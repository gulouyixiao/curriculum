

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;



-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id标识',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据字典名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据字典代码',
  `item_values` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '数据字典项--json格式',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tb_code_unique`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for media_files
-- ----------------------------
DROP TABLE IF EXISTS `media_files`;
CREATE TABLE `media_files`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件id,md5值',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '上传用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `file_type` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型（视频，图片）',
  `tags` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `bucket` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储目录',
  `timelength` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时长，单位时:分:秒',
  `file_path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储路径',
  `file_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件id',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '媒资文件访问地址',
  `create_date` datetime NULL DEFAULT NULL COMMENT '上传时间',
  `change_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `status` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态,1:正常，0:不展示',
  `remark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_fileid`(`file_id`) USING BTREE COMMENT '文件id唯一索引 '
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '媒资信息' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `order_main`;
CREATE TABLE `order_main`  (
  `id` bigint(20) NOT NULL COMMENT '主订单号',
  `total_price` float(8, 2) NOT NULL COMMENT '总价',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '600001' COMMENT '交易状态',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `order_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单名称',
  `order_descrip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单描述',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单主表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders_detail`;
CREATE TABLE `orders_detail`  (
  `id` bigint(20) NOT NULL COMMENT '订单号',
  `main_id` bigint(20) NOT NULL COMMENT '主订单号',
  `total_price` float(8, 2) NOT NULL COMMENT '总价',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '600001' COMMENT '交易状态',
  `shopping_type` varchar(2) COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品类型：01：周边，02：漫展演出',
  `out_business_id` bigint(20) NOT NULL COMMENT '具体商品id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `descrip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品描述',
  `unit_price` float(8, 2) NOT NULL COMMENT '单价',
  `order_number` int(11) NOT NULL DEFAULT 1 COMMENT '数量',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单子表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for acgn
-- ----------------------------
DROP TABLE IF EXISTS `acgn`;
CREATE TABLE `acgn`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `tags` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签，关键字',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `price` float(8, 2) NOT NULL COMMENT '价格',
  `vip_price` float(8, 2) NOT NULL COMMENT '会员价',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '漫展演出表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for surroundings
-- ----------------------------
DROP TABLE IF EXISTS `surroundings`;
CREATE TABLE `surroundings`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `price` float(8, 2) NOT NULL COMMENT '价格',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '品牌',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip',
  `vip_price` float(8, 2) NOT NULL COMMENT '会员价',
  `tags` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签，关键字',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '周边表' ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品名称',
  `pic` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `shopping_id` bigint NOT NULL COMMENT '商品id',
  `number` int NOT NULL DEFAULT '1' COMMENT '数量',
  `price` Double NOT NULL COMMENT '单价',
  `vip_price` Double NOT NULL COMMENT '会员价',
  `shopping_type` varchar(2) COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品类型：01：周边，02：漫展演出',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `remark` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT='购物车';

-- ----------------------------
-- Table structure for pay_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_record`;
CREATE TABLE `pay_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付交易号',
  `out_pay_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方支付交易流水号',
  `out_pay_channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方支付渠道编号',
  `order_id` bigint(20) NOT NULL COMMENT '商品订单号',
  `order_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单名称',
  `total_price` float(8, 2) NOT NULL COMMENT '订单总价单位元',
  `currency` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '币种CNY',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付状态',
  `pay_success_time` datetime NULL DEFAULT NULL COMMENT '支付成功时间',
  `user_id` bigint(20) UNSIGNED ZEROFILL NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pay_order_unique2`(`pay_no`) USING BTREE COMMENT '支付交易号',
  UNIQUE INDEX `pay_order_unique`(`out_pay_no`) USING BTREE COMMENT '第三方支付订单号'
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(96) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `grade` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '等级',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `userpic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `utype` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `birthday` datetime NULL DEFAULT NULL COMMENT '生日',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cellphone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户状态',
  `create_time` datetime NOT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_user_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1869681044449951747 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for video_audit
-- ----------------------------
DROP TABLE IF EXISTS `video_audit`;
CREATE TABLE `video_audit`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `video_id` bigint(20) NOT NULL COMMENT '视频id',
  `audit_mind` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核意见',
  `audit_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核状态',
  `audit_people` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `audit_date` datetime NULL DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '视频审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for video_base
-- ----------------------------
DROP TABLE IF EXISTS `video_base`;
CREATE TABLE `video_base`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `media_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '媒资文件id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '上传用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `tags` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签，关键字',
  `video_type` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频类型（普通视频，番剧）',
  `grade` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频等级，番剧级数',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '视频介绍',
  `cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面（路径）',
  `timelength` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时长，单位时:分:秒',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '媒资文件访问地址',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始播放时间',
  `parentid` bigint(20) NULL DEFAULT NULL COMMENT '父级Id',
  `style` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '204002' COMMENT '视频风格',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `change_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `thumbup_count` bigint(20) NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` bigint(20) NULL DEFAULT 0 COMMENT '评论数',
  `playback_volume` bigint(20) NULL DEFAULT 0 COMMENT '播放量',
  `member` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '201000' COMMENT '收费规则，201000：免费，201001：会员免费',
  `audit_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '202003' COMMENT '审核状态',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '203001' COMMENT '视频发布状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课程基本信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for video_comments
-- ----------------------------
DROP TABLE IF EXISTS `video_comments`;
CREATE TABLE `video_comments`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '评论用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `video_id` bigint(20) NOT NULL COMMENT '视频id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `create_date` datetime NULL DEFAULT NULL COMMENT '评论时间',
  `parent_comment_id` bigint(20) NULL DEFAULT NULL COMMENT '父评论ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '视频评论' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
