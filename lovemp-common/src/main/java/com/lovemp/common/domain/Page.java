package com.lovemp.common.domain;

import java.util.Collections;
import java.util.List;

/**
 * 分页结果对象
 *
 * @param <T> 分页项类型
 */
public class Page<T> {

    private final List<T> content;
    private final long totalElements;
    private final int page;
    private final int size;
    private final int totalPages;

    /**
     * 构造函数
     *
     * @param content 当前页内容
     * @param totalElements 总元素数
     * @param page 当前页码（从0开始）
     * @param size 每页大小
     */
    public Page(List<T> content, long totalElements, int page, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
        this.totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / (double) size);
    }

    /**
     * 创建空分页
     *
     * @param <T> 分页项类型
     * @return 空分页对象
     */
    public static <T> Page<T> empty() {
        return new Page<>(Collections.emptyList(), 0, 0, 0);
    }

    /**
     * 获取当前页内容
     *
     * @return 当前页内容列表
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * 获取总元素数
     *
     * @return 总元素数
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * 获取当前页码
     *
     * @return 当前页码
     */
    public int getPage() {
        return page;
    }

    /**
     * 获取每页大小
     *
     * @return 每页大小
     */
    public int getSize() {
        return size;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * 判断是否有内容
     *
     * @return 是否有内容
     */
    public boolean hasContent() {
        return !content.isEmpty();
    }

    /**
     * 判断是否是第一页
     *
     * @return 是否是第一页
     */
    public boolean isFirst() {
        return page == 0;
    }

    /**
     * 判断是否是最后一页
     *
     * @return 是否是最后一页
     */
    public boolean isLast() {
        return page == getTotalPages() - 1;
    }

    /**
     * 判断是否有下一页
     *
     * @return 是否有下一页
     */
    public boolean hasNext() {
        return !isLast();
    }

    /**
     * 判断是否有上一页
     *
     * @return 是否有上一页
     */
    public boolean hasPrevious() {
        return !isFirst();
    }
} 