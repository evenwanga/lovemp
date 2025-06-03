#!/bin/bash

# 自然人领域测试覆盖率报告生成脚本
# 作者: AI助手
# 创建日期: 2025-05-04

# 设置颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # 无颜色

# 打印带颜色的消息
print_message() {
  echo -e "${BLUE}[测试覆盖率]${NC} $1"
}

print_success() {
  echo -e "${GREEN}[成功]${NC} $1"
}

print_warning() {
  echo -e "${YELLOW}[警告]${NC} $1"
}

print_error() {
  echo -e "${RED}[错误]${NC} $1"
}

# 获取当前目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

print_message "开始生成自然人领域测试覆盖率报告..."
print_message "工作目录: ${PROJECT_ROOT}"

# 执行Maven命令生成测试覆盖率报告
print_message "执行测试并生成JaCoCo报告..."
cd "${PROJECT_ROOT}" && mvn clean test jacoco:report -pl lovemp-domain-person

# 检查命令执行结果
if [ $? -eq 0 ]; then
  print_success "JaCoCo测试覆盖率报告生成成功!"

  # 检查报告文件是否存在
  REPORT_DIR="${SCRIPT_DIR}/target/site/jacoco"
  REPORT_FILE="${REPORT_DIR}/index.html"

  if [ -f "${REPORT_FILE}" ]; then
    print_success "报告已保存到: ${REPORT_FILE}"
    
    # 尝试打开报告
    print_message "尝试在浏览器中打开报告..."
    
    if [[ "$OSTYPE" == "darwin"* ]]; then
      # macOS
      open "${REPORT_FILE}" && print_success "报告已在浏览器中打开"
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
      # Linux
      xdg-open "${REPORT_FILE}" 2>/dev/null || print_warning "无法自动打开报告，请手动打开: ${REPORT_FILE}"
    elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
      # Windows
      start "${REPORT_FILE}" || print_warning "无法自动打开报告，请手动打开: ${REPORT_FILE}"
    else
      print_warning "未知操作系统，请手动打开报告: ${REPORT_FILE}"
    fi
    
    # 检查中文化工具
    CHINESE_TOOL="${SCRIPT_DIR}/target/jacoco中文化书签.html"
    if [ -f "${CHINESE_TOOL}" ]; then
      print_message "中文化工具可用，可以使用它将报告翻译为中文"
      print_message "中文化工具路径: ${CHINESE_TOOL}"
    else
      print_warning "中文化工具不可用，将创建新的中文化工具"
      # 这里可以添加创建中文化工具的代码
    fi
    
    # 检查测试覆盖率摘要
    SUMMARY="${SCRIPT_DIR}/target/自然人领域测试覆盖率报告摘要.md"
    if [ -f "${SUMMARY}" ]; then
      print_message "测试覆盖率摘要已生成: ${SUMMARY}"
    else
      print_warning "测试覆盖率摘要未生成"
    fi
    
    # 显示一些基本的覆盖率数据
    print_message "正在提取覆盖率数据..."
    COVERAGE_DATA=$(grep -A 1 "<tfoot" "${REPORT_FILE}" | grep "bar" | sed 's/<[^>]*>//g' | tr -d '\n' | sed 's/^[ \t]*//;s/[ \t]*$//')
    echo -e "${BLUE}覆盖率摘要:${NC} ${COVERAGE_DATA}"
    
  else
    print_error "无法找到报告文件: ${REPORT_FILE}"
  fi
else
  print_error "生成JaCoCo测试覆盖率报告失败!"
fi

print_message "操作完成"
echo ""
print_message "提示：要将报告翻译为中文，请使用浏览器打开 ${SCRIPT_DIR}/target/jacoco中文化书签.html 并按照提示操作。" 