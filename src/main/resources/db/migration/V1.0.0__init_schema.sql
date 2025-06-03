-- 初始数据库结构脚本
-- 版本: 1.0.0
-- 作者: 系统架构组
-- 创建时间: 2023-08-01

-- 创建数据库
CREATE DATABASE IF NOT EXISTS person_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS brand_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS labor_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS customer_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS auth_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用自然人核心域数据库
USE person_db;

-- 创建person表（人员信息表）
CREATE TABLE IF NOT EXISTS person (
  id VARCHAR(36) NOT NULL COMMENT '主键ID',
  name VARCHAR(50) NOT NULL COMMENT '姓名',
  gender TINYINT NOT NULL COMMENT '性别：0-未知，1-男，2-女',
  birth_date DATE NOT NULL COMMENT '出生日期',
  id_card_type TINYINT NOT NULL COMMENT '证件类型：1-身份证，2-护照等',
  id_card_no VARCHAR(30) NOT NULL COMMENT '证件号码',
  mobile VARCHAR(20) NOT NULL COMMENT '手机号',
  email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  address VARCHAR(200) DEFAULT NULL COMMENT '地址',
  education TINYINT DEFAULT NULL COMMENT '学历：1-小学，2-初中，...',
  marital_status TINYINT DEFAULT NULL COMMENT '婚姻状态：0-未婚，1-已婚，2-离异，3-丧偶',
  version INT NOT NULL DEFAULT 0 COMMENT '版本号',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  created_by VARCHAR(36) NOT NULL COMMENT '创建人',
  created_time DATETIME NOT NULL COMMENT '创建时间',
  updated_by VARCHAR(36) NOT NULL COMMENT '更新人',
  updated_time DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_id_card (id_card_type, id_card_no),
  KEY idx_mobile (mobile),
  KEY idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员信息表';

-- 创建person_biometric表（人员生物特征表）
CREATE TABLE IF NOT EXISTS person_biometric (
  id VARCHAR(36) NOT NULL COMMENT '主键ID',
  person_id VARCHAR(36) NOT NULL COMMENT '人员ID',
  biometric_type TINYINT NOT NULL COMMENT '生物特征类型：1-人脸，2-指纹，3-虹膜',
  biometric_data BLOB NOT NULL COMMENT '生物特征数据',
  is_active TINYINT NOT NULL DEFAULT 1 COMMENT '是否激活：0-未激活，1-已激活',
  version INT NOT NULL DEFAULT 0 COMMENT '版本号',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  created_by VARCHAR(36) NOT NULL COMMENT '创建人',
  created_time DATETIME NOT NULL COMMENT '创建时间',
  updated_by VARCHAR(36) NOT NULL COMMENT '更新人',
  updated_time DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_person_id (person_id),
  KEY idx_person_biometric_type (person_id, biometric_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员生物特征表';

-- 使用品牌产品域数据库
USE brand_db;

-- 创建brand表（品牌表）
CREATE TABLE IF NOT EXISTS brand (
  id VARCHAR(36) NOT NULL COMMENT '主键ID',
  brand_code VARCHAR(30) NOT NULL COMMENT '品牌编码',
  brand_name VARCHAR(100) NOT NULL COMMENT '品牌名称',
  logo_url VARCHAR(255) DEFAULT NULL COMMENT '品牌Logo地址',
  contact_person VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  contact_phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  description TEXT DEFAULT NULL COMMENT '品牌描述',
  version INT NOT NULL DEFAULT 0 COMMENT '版本号',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  created_by VARCHAR(36) NOT NULL COMMENT '创建人',
  created_time DATETIME NOT NULL COMMENT '创建时间',
  updated_by VARCHAR(36) NOT NULL COMMENT '更新人',
  updated_time DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_brand_code (brand_code),
  KEY idx_brand_name (brand_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌表';

-- 其他表的创建语句省略，完整脚本参考数据库设计文档 