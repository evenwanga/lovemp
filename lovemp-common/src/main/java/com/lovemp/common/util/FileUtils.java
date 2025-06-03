package com.lovemp.common.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 * 
 * 提供全面的文件和目录操作方法，封装了Java IO和NIO的复杂操作，简化文件处理流程。
 * 
 * 主要功能包括：
 * 1. 文件读写：文本文件和二进制文件的读写操作
 * 2. 文件复制与移动：文件和目录的复制、移动
 * 3. 文件删除：文件和目录的递归删除
 * 4. 目录操作：创建目录、列出文件、获取目录大小等
 * 5. 文件信息：获取文件扩展名、基本名称、大小、修改时间等
 * 6. 文件格式转换：Base64编码与解码
 * 7. 压缩与解压：ZIP文件的压缩和解压缩
 * 8. 文件哈希：计算文件MD5、SHA256值
 * 9. 大文件处理：文件分片上传、断点续传、秒传功能
 * 10. 资源文件读取：从classpath读取资源文件
 * 
 * 适用场景：
 * - 需要进行文件读写、复制、移动等基本操作
 * - 需要处理大文件上传和下载
 * - 实现文件断点续传和秒传功能
 * - 需要对文件进行压缩与解压缩
 * - 需要验证文件完整性（通过哈希值比对）
 * - 处理系统中的临时文件
 * - 基于文件系统的资源管理
 * 
 * 使用示例：
 * // 读取文件内容
 * String content = FileUtils.readFileToString("/path/to/file.txt");
 * 
 * // 写入文件内容
 * FileUtils.writeStringToFile("/path/to/file.txt", "Hello World", false);
 * 
 * // 复制文件
 * FileUtils.copyFile("/source/file.txt", "/target/file.txt");
 * 
 * // 计算文件MD5值
 * String md5 = FileUtils.calculateFileMD5("/path/to/file.dat");
 * 
 * 注意：
 * 1. 方法抛出IOException时需要妥善处理异常
 * 2. 大文件操作时注意内存使用
 * 3. 文件路径使用时注意跨平台兼容性
 */
public final class FileUtils {

    private FileUtils() {
        // 工具类不允许实例化
    }

    /**
     * 默认缓冲区大小：8KB
     */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * 默认字符集：UTF-8
     */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    
    /**
     * 文件哈希值缓存，用于秒传功能
     */
    private static final Map<String, String> FILE_HASH_CACHE = new ConcurrentHashMap<>();
    
    /**
     * 已上传文件记录，用于秒传功能，键为文件哈希值，值为文件路径
     */
    private static final Map<String, String> UPLOADED_FILES = new ConcurrentHashMap<>();

    /**
     * 默认分片大小：2MB
     */
    private static final int DEFAULT_CHUNK_SIZE = 2 * 1024 * 1024;
    
    /**
     * 上传状态记录，用于断点续传功能
     * 键为文件的唯一标识，值为该文件的上传状态信息
     */
    private static final Map<String, UploadStatus> UPLOAD_STATUS_MAP = new ConcurrentHashMap<>();
    
    /**
     * 文件上传状态类，记录断点续传的状态信息
     */
    public static class UploadStatus {
        private final String fileId;         // 文件唯一标识
        private final String filePath;       // 目标文件路径
        private final long totalSize;        // 文件总大小
        private final int totalChunks;       // 总分片数
        private final boolean[] uploadedChunks; // 已上传分片标记
        private long uploadedSize;           // 已上传大小
        
        private UploadStatus(String fileId, String filePath, long totalSize, int totalChunks) {
            this.fileId = fileId;
            this.filePath = filePath;
            this.totalSize = totalSize;
            this.totalChunks = totalChunks;
            this.uploadedChunks = new boolean[totalChunks];
            this.uploadedSize = 0;
        }
        
        /**
         * 获取文件唯一标识
         */
        public String getFileId() {
            return fileId;
        }
        
        /**
         * 获取目标文件路径
         */
        public String getFilePath() {
            return filePath;
        }
        
        /**
         * 获取文件总大小
         */
        public long getTotalSize() {
            return totalSize;
        }
        
        /**
         * 获取总分片数
         */
        public int getTotalChunks() {
            return totalChunks;
        }
        
        /**
         * 获取已上传大小
         */
        public long getUploadedSize() {
            return uploadedSize;
        }
        
        /**
         * 设置已上传大小
         */
        public void setUploadedSize(long uploadedSize) {
            this.uploadedSize = uploadedSize;
        }
        
        /**
         * 标记分片为已上传
         */
        public void markChunkUploaded(int chunkIndex, long chunkSize) {
            if (chunkIndex >= 0 && chunkIndex < totalChunks) {
                uploadedChunks[chunkIndex] = true;
                uploadedSize += chunkSize;
            }
        }
        
        /**
         * 检查分片是否已上传
         */
        public boolean isChunkUploaded(int chunkIndex) {
            return chunkIndex >= 0 && chunkIndex < totalChunks && uploadedChunks[chunkIndex];
        }
        
        /**
         * 获取未上传的分片索引
         */
        public List<Integer> getNotUploadedChunks() {
            List<Integer> notUploaded = new ArrayList<>();
            for (int i = 0; i < totalChunks; i++) {
                if (!uploadedChunks[i]) {
                    notUploaded.add(i);
                }
            }
            return notUploaded;
        }
        
        /**
         * 检查是否所有分片都已上传
         */
        public boolean isCompleted() {
            for (boolean uploaded : uploadedChunks) {
                if (!uploaded) {
                    return false;
                }
            }
            return true;
        }
        
        /**
         * 获取上传进度（百分比）
         */
        public int getProgress() {
            if (totalSize == 0) {
                return 0;
            }
            return (int) (uploadedSize * 100 / totalSize);
        }
    }

    /**
     * 读取文件内容为字符串
     *
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException IO异常
     */
    public static String readFileToString(String filePath) throws IOException {
        return readFileToString(filePath, DEFAULT_CHARSET);
    }

    /**
     * 读取文件内容为字符串，可指定字符集
     *
     * @param filePath 文件路径
     * @param charset  字符集
     * @return 文件内容
     * @throws IOException IO异常
     */
    public static String readFileToString(String filePath, Charset charset) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        
        return Files.readString(Paths.get(filePath), charset);
    }

    /**
     * 读取文件内容为字节数组
     *
     * @param filePath 文件路径
     * @return 字节数组
     * @throws IOException IO异常
     */
    public static byte[] readFileToByteArray(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        
        return Files.readAllBytes(Paths.get(filePath));
    }

    /**
     * 读取文件内容为行列表
     *
     * @param filePath 文件路径
     * @return 行列表
     * @throws IOException IO异常
     */
    public static List<String> readLines(String filePath) throws IOException {
        return readLines(filePath, DEFAULT_CHARSET);
    }

    /**
     * 读取文件内容为行列表，可指定字符集
     *
     * @param filePath 文件路径
     * @param charset  字符集
     * @return 行列表
     * @throws IOException IO异常
     */
    public static List<String> readLines(String filePath, Charset charset) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return new ArrayList<>();
        }
        
        return Files.readAllLines(Paths.get(filePath), charset);
    }

    /**
     * 写字符串内容到文件
     *
     * @param filePath 文件路径
     * @param content  内容
     * @param append   是否追加
     * @throws IOException IO异常
     */
    public static void writeStringToFile(String filePath, String content, boolean append) throws IOException {
        writeStringToFile(filePath, content, DEFAULT_CHARSET, append);
    }

    /**
     * 写字符串内容到文件，可指定字符集
     *
     * @param filePath 文件路径
     * @param content  内容
     * @param charset  字符集
     * @param append   是否追加
     * @throws IOException IO异常
     */
    public static void writeStringToFile(String filePath, String content, Charset charset, boolean append) throws IOException {
        if (StringUtils.isEmpty(filePath) || content == null) {
            return;
        }
        
        File file = new File(filePath);
        createParentDirectories(file);
        
        OpenOption[] options = append 
                ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
                : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
        
        Files.writeString(file.toPath(), content, charset, options);
    }

    /**
     * 写字节数组到文件
     *
     * @param filePath 文件路径
     * @param data     字节数组
     * @param append   是否追加
     * @throws IOException IO异常
     */
    public static void writeByteArrayToFile(String filePath, byte[] data, boolean append) throws IOException {
        if (StringUtils.isEmpty(filePath) || data == null) {
            return;
        }
        
        File file = new File(filePath);
        createParentDirectories(file);
        
        OpenOption[] options = append 
                ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
                : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
        
        Files.write(file.toPath(), data, options);
    }

    /**
     * 写行列表到文件
     *
     * @param filePath 文件路径
     * @param lines    行列表
     * @param append   是否追加
     * @throws IOException IO异常
     */
    public static void writeLines(String filePath, List<String> lines, boolean append) throws IOException {
        writeLines(filePath, lines, DEFAULT_CHARSET, append);
    }

    /**
     * 写行列表到文件，可指定字符集
     *
     * @param filePath 文件路径
     * @param lines    行列表
     * @param charset  字符集
     * @param append   是否追加
     * @throws IOException IO异常
     */
    public static void writeLines(String filePath, List<String> lines, Charset charset, boolean append) throws IOException {
        if (StringUtils.isEmpty(filePath) || lines == null) {
            return;
        }
        
        File file = new File(filePath);
        createParentDirectories(file);
        
        OpenOption[] options = append 
                ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
                : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
        
        Files.write(file.toPath(), lines, charset, options);
    }

    /**
     * 复制文件
     *
     * @param sourceFilePath 源文件路径
     * @param targetFilePath 目标文件路径
     * @throws IOException IO异常
     */
    public static void copyFile(String sourceFilePath, String targetFilePath) throws IOException {
        if (StringUtils.isEmpty(sourceFilePath) || StringUtils.isEmpty(targetFilePath)) {
            return;
        }
        
        Path sourcePath = Paths.get(sourceFilePath);
        Path targetPath = Paths.get(targetFilePath);
        
        createParentDirectories(targetPath);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 复制目录
     *
     * @param sourceDir 源目录路径
     * @param targetDir 目标目录路径
     * @throws IOException IO异常
     */
    public static void copyDirectory(String sourceDir, String targetDir) throws IOException {
        if (StringUtils.isEmpty(sourceDir) || StringUtils.isEmpty(targetDir)) {
            return;
        }
        
        Path sourcePath = Paths.get(sourceDir);
        Path targetPath = Paths.get(targetDir);
        
        if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
            throw new IOException("源目录不存在或不是目录: " + sourceDir);
        }
        
        createDirectories(targetDir);
        
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = targetPath.resolve(sourcePath.relativize(dir));
                try {
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                } catch (FileAlreadyExistsException e) {
                    if (!Files.isDirectory(targetDir)) {
                        throw e;
                    }
                    return FileVisitResult.CONTINUE;
                }
            }
            
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), 
                        StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 移动文件
     *
     * @param sourceFilePath 源文件路径
     * @param targetFilePath 目标文件路径
     * @throws IOException IO异常
     */
    public static void moveFile(String sourceFilePath, String targetFilePath) throws IOException {
        if (StringUtils.isEmpty(sourceFilePath) || StringUtils.isEmpty(targetFilePath)) {
            return;
        }
        
        Path sourcePath = Paths.get(sourceFilePath);
        Path targetPath = Paths.get(targetFilePath);
        
        createParentDirectories(targetPath);
        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除目录及其内容
     *
     * @param directoryPath 目录路径
     * @return 是否删除成功
     */
    public static boolean deleteDirectory(String directoryPath) {
        if (StringUtils.isEmpty(directoryPath)) {
            return false;
        }
        
        try {
            Path directory = Paths.get(directoryPath);
            if (Files.exists(directory)) {
                Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }
                    
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 创建目录
     *
     * @param directoryPath 目录路径
     * @throws IOException IO异常
     */
    public static void createDirectories(String directoryPath) throws IOException {
        if (StringUtils.isEmpty(directoryPath)) {
            return;
        }
        
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    /**
     * 创建父目录
     *
     * @param file 文件
     * @throws IOException IO异常
     */
    public static void createParentDirectories(File file) throws IOException {
        if (file == null) {
            return;
        }
        
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("无法创建目录: " + parent);
        }
    }

    /**
     * 创建父目录
     *
     * @param path 路径
     * @throws IOException IO异常
     */
    public static void createParentDirectories(Path path) throws IOException {
        if (path == null) {
            return;
        }
        
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getFileExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 获取不带扩展名的文件名
     *
     * @param fileName 文件名
     * @return 不带扩展名的文件名
     */
    public static String getFileBaseName(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    public static boolean exists(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 检查是否为文件
     *
     * @param path 路径
     * @return 是否为文件
     */
    public static boolean isFile(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        
        return Files.isRegularFile(Paths.get(path));
    }

    /**
     * 检查是否为目录
     *
     * @param path 路径
     * @return 是否为目录
     */
    public static boolean isDirectory(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        
        return Files.isDirectory(Paths.get(path));
    }

    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小（字节）
     * @throws IOException IO异常
     */
    public static long getFileSize(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return 0;
        }
        
        return Files.size(Paths.get(filePath));
    }

    /**
     * 获取目录大小
     *
     * @param directoryPath 目录路径
     * @return 目录大小（字节）
     * @throws IOException IO异常
     */
    public static long getDirectorySize(String directoryPath) throws IOException {
        if (StringUtils.isEmpty(directoryPath)) {
            return 0;
        }
        
        Path directory = Paths.get(directoryPath);
        if (!Files.isDirectory(directory)) {
            return 0;
        }
        
        final long[] size = {0};
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                size[0] += attrs.size();
                return FileVisitResult.CONTINUE;
            }
        });
        
        return size[0];
    }

    /**
     * 列出目录下的文件和子目录
     *
     * @param directoryPath 目录路径
     * @return 文件和目录列表
     * @throws IOException IO异常
     */
    public static List<String> listFiles(String directoryPath) throws IOException {
        if (StringUtils.isEmpty(directoryPath)) {
            return new ArrayList<>();
        }
        
        Path directory = Paths.get(directoryPath);
        if (!Files.isDirectory(directory)) {
            return new ArrayList<>();
        }
        
        List<String> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                result.add(path.getFileName().toString());
            }
        }
        
        return result;
    }

    /**
     * 获取临时文件路径
     *
     * @param prefix    前缀
     * @param suffix    后缀
     * @param directory 目录（可为null，表示系统临时目录）
     * @return 临时文件路径
     * @throws IOException IO异常
     */
    public static String getTempFilePath(String prefix, String suffix, String directory) throws IOException {
        Path dir = directory == null ? null : Paths.get(directory);
        Path tempFile = dir == null 
                ? Files.createTempFile(prefix, suffix)
                : Files.createTempFile(dir, prefix, suffix);
        
        return tempFile.toString();
    }

    /**
     * 获取基于UUID的随机文件名
     *
     * @param extension 文件扩展名
     * @return 随机文件名
     */
    public static String getRandomFileName(String extension) {
        String uuid = UUID.randomUUID().toString(); // 使用原始UUID包含短横线，长度为36
        return StringUtils.isEmpty(extension) ? uuid : uuid + "." + extension;
    }

    /**
     * 文件转Base64字符串
     *
     * @param filePath 文件路径
     * @return Base64字符串
     * @throws IOException IO异常
     */
    public static String fileToBase64(String filePath) throws IOException {
        byte[] bytes = readFileToByteArray(filePath);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Base64字符串转文件
     *
     * @param base64    Base64字符串
     * @param filePath  文件路径
     * @throws IOException IO异常
     */
    public static void base64ToFile(String base64, String filePath) throws IOException {
        if (StringUtils.isEmpty(base64) || StringUtils.isEmpty(filePath)) {
            return;
        }
        
        byte[] bytes = Base64.getDecoder().decode(base64);
        writeByteArrayToFile(filePath, bytes, false);
    }

    /**
     * 压缩文件或目录
     *
     * @param sourcePath 源文件或目录路径
     * @param zipPath    ZIP文件路径
     * @throws IOException IO异常
     */
    public static void zip(String sourcePath, String zipPath) throws IOException {
        if (StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(zipPath)) {
            return;
        }
        
        Path source = Paths.get(sourcePath);
        Path zipFile = Paths.get(zipPath);
        
        createParentDirectories(zipFile);
        
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            if (Files.isDirectory(source)) {
                zipDirectory(source, source.getFileName().toString(), zos);
            } else {
                zipFile(source, source.getFileName().toString(), zos);
            }
        }
    }

    /**
     * 压缩目录
     */
    private static void zipDirectory(Path directory, String baseName, ZipOutputStream zos) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String entryName = baseName + "/" + directory.relativize(file).toString().replace('\\', '/');
                zipFile(file, entryName, zos);
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (!dir.equals(directory)) {
                    String entryName = baseName + "/" + directory.relativize(dir).toString().replace('\\', '/') + "/";
                    zos.putNextEntry(new ZipEntry(entryName));
                    zos.closeEntry();
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 压缩文件
     */
    private static void zipFile(Path file, String entryName, ZipOutputStream zos) throws IOException {
        if (Files.isRegularFile(file)) {
            zos.putNextEntry(new ZipEntry(entryName));
            Files.copy(file, zos);
            zos.closeEntry();
        }
    }

    /**
     * 解压文件
     *
     * @param zipPath        ZIP文件路径
     * @param extractPath    解压目标路径
     * @throws IOException IO异常
     */
    public static void unzip(String zipPath, String extractPath) throws IOException {
        if (StringUtils.isEmpty(zipPath) || StringUtils.isEmpty(extractPath)) {
            return;
        }
        
        Path targetDir = Paths.get(extractPath);
        createDirectories(extractPath);
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path path = targetDir.resolve(entry.getName());
                
                if (entry.isDirectory()) {
                    Files.createDirectories(path);
                } else {
                    createParentDirectories(path);
                    Files.copy(zis, path, StandardCopyOption.REPLACE_EXISTING);
                }
                
                zis.closeEntry();
            }
        }
    }

    /**
     * 获取文件或目录的最后修改时间
     *
     * @param path 路径
     * @return 最后修改时间（毫秒）
     * @throws IOException IO异常
     */
    public static long getLastModifiedTime(String path) throws IOException {
        if (StringUtils.isEmpty(path)) {
            return 0;
        }
        
        return Files.getLastModifiedTime(Paths.get(path)).toMillis();
    }

    /**
     * 读取资源文件内容为字符串
     *
     * @param resourcePath 资源路径
     * @return 资源内容
     * @throws IOException IO异常
     */
    public static String readResourceAsString(String resourcePath) throws IOException {
        if (StringUtils.isEmpty(resourcePath)) {
            return null;
        }
        
        try (InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException("资源文件未找到: " + resourcePath);
            }
            return readInputStreamToString(is, DEFAULT_CHARSET);
        }
    }

    /**
     * 读取输入流内容为字符串
     *
     * @param is      输入流
     * @param charset 字符集
     * @return 内容字符串
     * @throws IOException IO异常
     */
    public static String readInputStreamToString(InputStream is, Charset charset) throws IOException {
        if (is == null) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        
        try (BufferedInputStream bis = new BufferedInputStream(is)) {
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, bytesRead, charset));
            }
        }
        
        return sb.toString();
    }

    /**
     * 计算文件的MD5哈希值
     *
     * @param filePath 文件路径
     * @return MD5哈希值
     * @throws IOException IO异常
     */
    public static String calculateFileMD5(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        
        // 首先检查缓存中是否已有该文件的哈希值
        if (FILE_HASH_CACHE.containsKey(filePath)) {
            return FILE_HASH_CACHE.get(filePath);
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream is = Files.newInputStream(Paths.get(filePath));
                 DigestInputStream dis = new DigestInputStream(is, md)) {
                
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                while (dis.read(buffer) != -1) {
                    // 读取文件内容，自动更新MessageDigest
                }
                
                byte[] digest = md.digest();
                String md5Hex = bytesToHex(digest);
                
                // 将结果存入缓存
                FILE_HASH_CACHE.put(filePath, md5Hex);
                
                return md5Hex;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("计算MD5哈希值失败", e);
        }
    }
    
    /**
     * 计算文件的SHA-256哈希值
     *
     * @param filePath 文件路径
     * @return SHA-256哈希值
     * @throws IOException IO异常
     */
    public static String calculateFileSHA256(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            try (InputStream is = Files.newInputStream(Paths.get(filePath));
                 DigestInputStream dis = new DigestInputStream(is, md)) {
                
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                while (dis.read(buffer) != -1) {
                    // 读取文件内容，自动更新MessageDigest
                }
                
                byte[] digest = md.digest();
                return bytesToHex(digest);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("计算SHA-256哈希值失败", e);
        }
    }
    
    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    /**
     * 注册上传文件，用于秒传功能
     *
     * @param filePath 文件路径
     * @return 文件的哈希值
     * @throws IOException IO异常
     */
    public static String registerUploadedFile(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath) || !Files.exists(Paths.get(filePath))) {
            throw new IOException("文件不存在: " + filePath);
        }
        
        String fileHash = calculateFileMD5(filePath);
        UPLOADED_FILES.put(fileHash, filePath);
        return fileHash;
    }
    
    /**
     * 检查文件是否可以秒传
     *
     * @param fileHash 文件哈希值
     * @return 如果文件可以秒传则返回true，否则返回false
     */
    public static boolean canFastUpload(String fileHash) {
        return fileHash != null && UPLOADED_FILES.containsKey(fileHash);
    }
    
    /**
     * 获取已上传文件的路径
     *
     * @param fileHash 文件哈希值
     * @return 文件路径，如果文件不存在则返回null
     */
    public static String getUploadedFilePath(String fileHash) {
        return UPLOADED_FILES.get(fileHash);
    }
    
    /**
     * 执行文件秒传
     *
     * @param fileHash 文件哈希值
     * @param targetPath 目标路径
     * @return 是否成功
     * @throws IOException IO异常
     */
    public static boolean fastUpload(String fileHash, String targetPath) throws IOException {
        if (!canFastUpload(fileHash)) {
            return false;
        }
        
        String sourcePath = getUploadedFilePath(fileHash);
        if (sourcePath == null || !Files.exists(Paths.get(sourcePath))) {
            // 源文件不存在，移除无效记录
            UPLOADED_FILES.remove(fileHash);
            return false;
        }
        
        // 复制文件到目标路径
        copyFile(sourcePath, targetPath);
        return true;
    }
    
    /**
     * 清除文件哈希缓存
     */
    public static void clearFileHashCache() {
        FILE_HASH_CACHE.clear();
    }
    
    /**
     * 清除指定文件的哈希缓存
     *
     * @param filePath 文件路径
     */
    public static void removeFileHashCache(String filePath) {
        if (StringUtils.isNotEmpty(filePath)) {
            FILE_HASH_CACHE.remove(filePath);
        }
    }
    
    /**
     * 保存文件哈希记录到文件
     *
     * @param filePath 保存路径
     * @throws IOException IO异常
     */
    public static void saveFileHashRecord(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return;
        }
        
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, String> entry : UPLOADED_FILES.entrySet()) {
            lines.add(entry.getKey() + "," + entry.getValue());
        }
        
        writeLines(filePath, lines, false);
    }
    
    /**
     * 从文件加载文件哈希记录
     *
     * @param filePath 文件路径
     * @throws IOException IO异常
     */
    public static void loadFileHashRecord(String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath) || !Files.exists(Paths.get(filePath))) {
            return;
        }
        
        List<String> lines = readLines(filePath);
        for (String line : lines) {
            String[] parts = line.split(",", 2);
            if (parts.length == 2) {
                UPLOADED_FILES.put(parts[0], parts[1]);
            }
        }
    }

    /**
     * 初始化文件上传，准备断点续传
     *
     * @param fileId 文件唯一标识（可以是文件名、MD5等）
     * @param filePath 目标文件路径
     * @param totalSize 文件总大小
     * @return 上传状态对象
     */
    public static UploadStatus initUpload(String fileId, String filePath, long totalSize) {
        return initUpload(fileId, filePath, totalSize, DEFAULT_CHUNK_SIZE);
    }
    
    /**
     * 初始化文件上传，准备断点续传
     *
     * @param fileId 文件唯一标识（可以是文件名、MD5等）
     * @param filePath 目标文件路径
     * @param totalSize 文件总大小
     * @param chunkSize 分片大小
     * @return 上传状态对象
     */
    public static UploadStatus initUpload(String fileId, String filePath, long totalSize, int chunkSize) {
        if (StringUtils.isEmpty(fileId) || StringUtils.isEmpty(filePath) || totalSize <= 0) {
            throw new IllegalArgumentException("参数错误");
        }
        
        int totalChunks = (int) Math.ceil((double) totalSize / chunkSize);
        UploadStatus status = new UploadStatus(fileId, filePath, totalSize, totalChunks);
        UPLOAD_STATUS_MAP.put(fileId, status);
        
        // 创建临时目录用于存放分片
        String chunkDir = getChunkDirectory(fileId);
        try {
            createDirectories(chunkDir);
        } catch (IOException e) {
            throw new RuntimeException("创建分片目录失败", e);
        }
        
        return status;
    }
    
    /**
     * 获取上传状态
     *
     * @param fileId 文件唯一标识
     * @return 上传状态对象，如果不存在则返回null
     */
    public static UploadStatus getUploadStatus(String fileId) {
        return UPLOAD_STATUS_MAP.get(fileId);
    }
    
    /**
     * 上传文件分片
     *
     * @param fileId 文件唯一标识
     * @param chunkIndex 分片索引
     * @param chunkData 分片数据
     * @return 是否上传成功
     * @throws IOException IO异常
     */
    public static boolean uploadChunk(String fileId, int chunkIndex, byte[] chunkData) throws IOException {
        UploadStatus status = getUploadStatus(fileId);
        if (status == null) {
            throw new IllegalArgumentException("未找到上传状态: " + fileId);
        }
        
        if (chunkIndex < 0 || chunkIndex >= status.getTotalChunks()) {
            throw new IllegalArgumentException("分片索引无效: " + chunkIndex);
        }
        
        if (status.isChunkUploaded(chunkIndex)) {
            return true; // 分片已上传，直接返回成功
        }
        
        String chunkPath = getChunkPath(fileId, chunkIndex);
        writeByteArrayToFile(chunkPath, chunkData, false);
        
        status.markChunkUploaded(chunkIndex, chunkData.length);
        return true;
    }
    
    /**
     * 上传文件分片
     *
     * @param fileId 文件唯一标识
     * @param chunkIndex 分片索引
     * @param chunkFile 分片文件
     * @return 是否上传成功
     * @throws IOException IO异常
     */
    public static boolean uploadChunk(String fileId, int chunkIndex, File chunkFile) throws IOException {
        if (!chunkFile.exists() || !chunkFile.isFile()) {
            throw new FileNotFoundException("分片文件不存在: " + chunkFile.getPath());
        }
        
        UploadStatus status = getUploadStatus(fileId);
        if (status == null) {
            throw new IllegalArgumentException("未找到上传状态: " + fileId);
        }
        
        if (chunkIndex < 0 || chunkIndex >= status.getTotalChunks()) {
            throw new IllegalArgumentException("分片索引无效: " + chunkIndex);
        }
        
        if (status.isChunkUploaded(chunkIndex)) {
            return true; // 分片已上传，直接返回成功
        }
        
        String chunkPath = getChunkPath(fileId, chunkIndex);
        copyFile(chunkFile.getPath(), chunkPath);
        
        status.markChunkUploaded(chunkIndex, chunkFile.length());
        return true;
    }
    
    /**
     * 完成文件上传，合并所有分片
     *
     * @param fileId 文件唯一标识
     * @return 是否成功
     * @throws IOException IO异常
     */
    public static boolean completeUpload(String fileId) throws IOException {
        UploadStatus status = getUploadStatus(fileId);
        if (status == null) {
            throw new IllegalArgumentException("未找到上传状态: " + fileId);
        }
        
        if (!status.isCompleted()) {
            List<Integer> notUploaded = status.getNotUploadedChunks();
            throw new IllegalStateException("文件未完成上传，还有" + notUploaded.size() + "个分片未上传");
        }
        
        // 创建目标文件的父目录
        Path targetPath = Paths.get(status.getFilePath());
        createParentDirectories(targetPath);
        
        // 合并分片
        try (FileChannel targetChannel = FileChannel.open(targetPath, 
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            
            for (int i = 0; i < status.getTotalChunks(); i++) {
                String chunkPath = getChunkPath(fileId, i);
                Path chunkFilePath = Paths.get(chunkPath);
                
                try (FileChannel sourceChannel = FileChannel.open(chunkFilePath, StandardOpenOption.READ)) {
                    long position = targetChannel.position();
                    sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
                    targetChannel.position(position + sourceChannel.size());
                }
            }
        }
        
        // 删除分片目录
        deleteDirectory(getChunkDirectory(fileId));
        
        // 移除上传状态
        UPLOAD_STATUS_MAP.remove(fileId);
        
        return true;
    }
    
    /**
     * 取消文件上传，删除所有分片
     *
     * @param fileId 文件唯一标识
     * @return 是否成功
     */
    public static boolean cancelUpload(String fileId) {
        UploadStatus status = getUploadStatus(fileId);
        if (status == null) {
            return false;
        }
        
        // 删除分片目录
        deleteDirectory(getChunkDirectory(fileId));
        
        // 移除上传状态
        UPLOAD_STATUS_MAP.remove(fileId);
        
        return true;
    }
    
    /**
     * 获取分片目录路径
     *
     * @param fileId 文件唯一标识
     * @return 分片目录路径
     */
    private static String getChunkDirectory(String fileId) {
        return System.getProperty("java.io.tmpdir") + File.separator + "chunks" + File.separator + fileId;
    }
    
    /**
     * 获取分片文件路径
     *
     * @param fileId 文件唯一标识
     * @param chunkIndex 分片索引
     * @return 分片文件路径
     */
    private static String getChunkPath(String fileId, int chunkIndex) {
        return getChunkDirectory(fileId) + File.separator + chunkIndex;
    }
    
    /**
     * 将文件分割成多个分片
     *
     * @param filePath 文件路径
     * @param chunkSize 分片大小（字节）
     * @return 分片文件路径列表
     * @throws IOException IO异常
     */
    public static List<String> splitFileToChunks(String filePath, int chunkSize) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return new ArrayList<>();
        }
        
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("文件不存在: " + filePath);
        }
        
        long fileSize = Files.size(path);
        if (fileSize == 0) {
            return new ArrayList<>();
        }
        
        String fileId = UUID.randomUUID().toString();
        String chunkDir = getChunkDirectory(fileId);
        createDirectories(chunkDir);
        
        int totalChunks = (int) Math.ceil((double) fileSize / chunkSize);
        List<String> chunkPaths = new ArrayList<>(totalChunks);
        
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[chunkSize];
            int bytesRead;
            int chunkIndex = 0;
            
            while ((bytesRead = fis.read(buffer)) > 0) {
                String chunkPath = getChunkPath(fileId, chunkIndex);
                if (bytesRead < chunkSize) {
                    // 最后一个分片可能不足chunkSize
                    byte[] lastChunk = new byte[bytesRead];
                    System.arraycopy(buffer, 0, lastChunk, 0, bytesRead);
                    writeByteArrayToFile(chunkPath, lastChunk, false);
                } else {
                    writeByteArrayToFile(chunkPath, buffer, false);
                }
                
                chunkPaths.add(chunkPath);
                chunkIndex++;
            }
        }
        
        return chunkPaths;
    }
    
    /**
     * 将多个分片合并成一个文件
     *
     * @param chunkPaths 分片文件路径列表
     * @param targetFilePath 目标文件路径
     * @throws IOException IO异常
     */
    public static void mergeChunksToFile(List<String> chunkPaths, String targetFilePath) throws IOException {
        if (CollectionUtils.isEmpty(chunkPaths) || StringUtils.isEmpty(targetFilePath)) {
            return;
        }
        
        Path targetPath = Paths.get(targetFilePath);
        createParentDirectories(targetPath);
        
        try (FileChannel targetChannel = FileChannel.open(targetPath, 
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            
            for (String chunkPath : chunkPaths) {
                Path chunkFilePath = Paths.get(chunkPath);
                if (!Files.exists(chunkFilePath)) {
                    throw new FileNotFoundException("分片文件不存在: " + chunkPath);
                }
                
                try (FileChannel sourceChannel = FileChannel.open(chunkFilePath, StandardOpenOption.READ)) {
                    long position = targetChannel.position();
                    sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
                    targetChannel.position(position + sourceChannel.size());
                }
            }
        }
    }
} 