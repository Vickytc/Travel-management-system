/*
 Navicat MySQL Data Transfer

 Source Server         : docker_mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:8000
 Source Schema         : travel-admin

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 07/10/2024 18:28:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Comment
-- ----------------------------
DROP TABLE IF EXISTS `Comment`;
CREATE TABLE `Comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Comment ID',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Comment content',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Comment time',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Commenter email',
  `hotel_id` int(11) NOT NULL COMMENT 'Hotel ID',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Avatar',
  `room_id` int(11) NOT NULL COMMENT 'Room ID',
  `service_rating` int(11) NOT NULL COMMENT 'Service rating',
  `state` int(11) NOT NULL COMMENT 'Comment status, 0 is unreviewed, 1 is approved',
  `user_id` int(11) NOT NULL COMMENT 'Commenter ID',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Commenter name',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Comment information table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of Comment
-- ----------------------------
INSERT INTO `Comment` VALUES (2, 'The stay experience was excellent', '2024-10-05 08:21:42', 'lisi@qq.com', 2, '/api/image/68087cae2cef44739edbecf02fd2fb51.jpg', 1, 4, 1, 5, 'Li Si');
INSERT INTO `Comment` VALUES (4, 'Newly renovated hotel, very good experience', '2024-10-06 22:49:53', 'luxibai@163.com', 7, '/api/image/a1.jpg', 13, 5, 1, 12, 'zhansan');
INSERT INTO `Comment` VALUES (5, 'very good', '2024-10-07 18:00:51', '1229837368@qq.com', 4, '/api/image/aa6129a07cb34112b5f82109ec7593e0.jpg', 9, 5, 1, 4, 'ZhangSan');
INSERT INTO `Comment` VALUES (6, 'good!', '2024-10-07 18:17:43', 'luxibai@163.com', 4, '/api/image/aa6129a07cb34112b5f82109ec7593e0.jpg', 8, 5, 1, 4, 'ZhangSan');
INSERT INTO `Comment` VALUES (7, 'HHAHHHH', '2024-10-07 18:21:17', 'luxibai@163.com', 3, '/api/image/aa6129a07cb34112b5f82109ec7593e0.jpg', 7, 5, 1, 4, 'ZhangSan');

-- ----------------------------
-- Table structure for Hotel
-- ----------------------------
DROP TABLE IF EXISTS `Hotel`;
CREATE TABLE `Hotel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Hotel ID',
  `business_registration_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hotel Business Registration Number',
  `contact_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hotel Contact Email',
  `contact_phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hotel Contact Phone Number',
  `contact_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hotel Contact Name',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hotel Description',
  `hotel_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hotel Address',
  `hotel_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hotel Name',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Hotel Image',
  `star_rating` int(11) NOT NULL COMMENT 'Hotel Star Rating',
  `state` int(11) NOT NULL COMMENT 'Hotel Status: 0 is Open, 1 is Closed',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Hotel Official Website URL',
  `user_id` int(11) NOT NULL COMMENT 'Owner ID of the Hotel',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Hotel Information Table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of Hotel
-- ----------------------------
INSERT INTO `Hotel` VALUES (2, '017-06415323', 's.nmtssy@mva.lb', '18681547681', 'Liao Na', '2024-10-05 03:08:12', 'Five-Star Luxury Hotel', 'Los Angeles, USA', 'Vienna Hotel', '/api/image/09b75c6b87a94ad080d5ee89108f2163.jpg', 5, 0, 'http://hyobjksmsu.af/iihb', 2);
INSERT INTO `Hotel` VALUES (3, '2234-1233', 'lisi@qq.com', '13415552223', 'Li Si', '2024-10-05 14:26:01', 'Four-Star Hotel', 'Chicago, USA', 'Quanjing Hotel', '/api/image/65e87948209147b5bb67ecca9c9dbb9f.jpg', 4, 0, 'https://quanji.com', 5);
INSERT INTO `Hotel` VALUES (4, '017-06415323', 's.nmtssy@mva.lb', '18681547681', 'Liao Na', '2024-10-05 17:38:51', 'Four-Star Luxury Hotel', 'Houston, USA', 'Sanjiang Hotel', '/api/image/140308b7a8d343f6b5c728707871b802.jpg', 4, 0, 'https://sanjiang.com', 2);
INSERT INTO `Hotel` VALUES (5, '2234-1233', 'lisi@qq.com', '13415552223', 'Li Si', '2024-10-05 17:39:37', 'Five-Star Luxury Hotel', 'Miami, USA', 'Four Seasons Hotel', '/api/image/3d985e29e68b4817bba63b513b41fe00.jpg', 5, 0, 'https://siji.com', 5);
INSERT INTO `Hotel` VALUES (6, '017-06415323', 's.nmtssy@mva.lb', '18681547681', 'Liao Na', '2024-10-05 17:41:35', 'Four-Star Luxury Hotel', 'Newark, USA', 'Hehuan Hotel', '/api/image/41d3b2ecf64349f3ac656658fbbaf043.jpg', 4, 0, 'https://hehuan.com', 2);
INSERT INTO `Hotel` VALUES (7, '2234-1233', 'lisi@qq.com', '13415552223', 'Li Si', '2024-10-05 17:42:55', 'Five-Star Luxury Hotel', 'New York, USA', 'Kaiguan Hotel', '/api/image/0291a1b366454042a11bdd945f1c1690.jpg', 5, 0, 'https://gaiguan.com', 5);
INSERT INTO `Hotel` VALUES (8, '2234-1233', 'lisi@qq.com', '13415552223', 'Li Si', '2024-10-05 17:43:50', 'Three-Star Hotel', 'San Diego, USA', 'Jinqiao Hotel', '/api/image/990e171e34fe4656a74794f4e19e3f5d.jpg', 3, 0, 'https://jingqiao.com', 5);
INSERT INTO `Hotel` VALUES (9, '017-06415323', 's.nmtssy@mva.lb', '18681547681', 'Liao Na', '2024-10-05 17:44:50', 'Five-Star Luxury Hotel', 'Washington, USA', 'Mingzhu Hotel', '/api/image/29b1cf2eac894400ba0da9d71c01e233.jpg', 5, 0, 'https://mingzhu.com', 2);

-- ----------------------------
-- Table structure for Reserve
-- ----------------------------
DROP TABLE IF EXISTS `Reserve`;
CREATE TABLE `Reserve`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Reservation ID',
  `comment_id` int(11) NULL DEFAULT NULL COMMENT 'Comment ID',
  `count` int(11) NOT NULL COMMENT 'Reservation Quantity',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Reserver Email',
  `end_date` date NOT NULL COMMENT 'Check-out Date',
  `hotel_id` int(11) NOT NULL COMMENT 'Reserved Hotel ID',
  `meal_plans` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Meal Plans',
  `room_id` int(11) NOT NULL COMMENT 'Reserved Room ID',
  `start_date` date NOT NULL COMMENT 'Check-in Date',
  `state` int(11) NOT NULL COMMENT 'Reservation Status: 0 is CONFIRMED, 1 is ON HOLD, 2 is CANCELLED',
  `user_id` int(11) NOT NULL COMMENT 'Reserver ID',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Reserver Name',
  `total_amount` decimal(10, 2) NOT NULL COMMENT 'Total Amount',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Reservation Information Table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of Reserve
-- ----------------------------
INSERT INTO `Reserve` VALUES (5, NULL, 2, '2024-10-05 15:28:54', 'luxibai@163.com', '2024-10-07', 2, '[\"cake\"]', 1, '2024-10-05', 2, 3, 'Cao Jie', 442.20);
INSERT INTO `Reserve` VALUES (6, 3, 2, '2024-10-05 18:57:24', 'luxibai@163.com', '2024-10-06', 2, '[\"rice\", \"buns\"]', 1, '2024-10-05', 2, 6, 'Wang Wu', 442.20);
INSERT INTO `Reserve` VALUES (7, NULL, 2, '2024-10-04 19:51:14', 'luxibai@163.com', '2024-10-08', 2, '[\"rice\"]', 1, '2024-10-07', 0, 6, 'Wang Wu', 442.20);
INSERT INTO `Reserve` VALUES (8, NULL, 1, '2024-10-04 18:49:08', 'luxibai@163.com', '2024-10-08', 2, '[\"rice\"]', 1, '2024-10-07', 0, 3, 'Cao Jie', 221.10);
INSERT INTO `Reserve` VALUES (10, 6, 3, '2024-10-03 18:49:08', 'luxibai@163.com', '2024-10-10', 4, '[\"fried dough sticks\"]', 8, '2024-10-09', 0, 2, 'Liao Na', 825.00);
INSERT INTO `Reserve` VALUES (11, NULL, 1, '2024-10-03 18:49:08', 'luxibai@163.com', '2024-10-11', 5, '[\"bread\"]', 10, '2024-10-10', 2, 6, 'Zhang San', 495.00);
INSERT INTO `Reserve` VALUES (13, NULL, 4, '2024-10-02 18:49:08', 'luxibai@163.com', '2024-10-13', 7, '[\"buns\"]', 14, '2024-10-12', 2, 3, 'Wang Wu', 8800.00);
INSERT INTO `Reserve` VALUES (15, NULL, 5, '2024-10-02 18:49:08', 'luxibai@163.com', '2024-10-15', 9, '[\"rice\"]', 18, '2024-10-14', 2, 2, 'Zhang San', 1320.00);
INSERT INTO `Reserve` VALUES (16, NULL, 3, '2024-10-02 18:49:08', 'luxibai@163.com', '2024-10-16', 2, '[\"bread\"]', 1, '2024-10-15', 2, 3, 'Cao Jie', 663.30);
INSERT INTO `Reserve` VALUES (17, NULL, 4, '2024-10-01 18:49:08', 'luxibai@163.com', '2024-10-17', 5, '[\"buns\"]', 6, '2024-10-16', 0, 5, 'Li Si', 2156.00);
INSERT INTO `Reserve` VALUES (18, NULL, 2, '2024-10-01 18:49:08', 'luxibai@163.com', '2024-10-18', 3, '[\"fried dough sticks\"]', 7, '2024-10-17', 0, 6, 'Liao Na', 4180.00);
INSERT INTO `Reserve` VALUES (19, NULL, 1, '2024-10-01 18:49:08', 'luxibai@163.com', '2024-10-19', 2, '[\"rice\"]', 10, '2024-10-18', 1, 3, 'Zhang San', 495.00);
INSERT INTO `Reserve` VALUES (22, 8, 4, '2024-09-30 18:49:08', 'luxibai@163.com', '2024-10-22', 7, '[\"fried dough sticks\"]', 14, '2024-10-21', 0, 4, 'Wang Wu', 2156.00);
INSERT INTO `Reserve` VALUES (23, NULL, 2, '2024-09-29 18:49:08', 'luxibai@163.com', '2024-10-23', 9, '[\"rice\"]', 12, '2024-10-22', 1, 5, 'Zhang San', 220.00);
INSERT INTO `Reserve` VALUES (24, NULL, 1, '2024-09-30 18:49:08', 'luxibai@163.com', '2024-10-24', 3, '[\"bread\"]', 8, '2024-10-23', 2, 6, 'Li Si', 374.00);
INSERT INTO `Reserve` VALUES (25, NULL, 2, '2024-09-30 18:49:08', 'luxibai@163.com', '2024-10-25', 4, '[\"buns\"]', 5, '2024-10-24', 0, 2, 'Cao Jie', 990.00);
INSERT INTO `Reserve` VALUES (26, NULL, 3, '2024-09-28 10:05:12', 'luxibai@163.com', '2024-10-03', 2, '[\"rice\", \"buns\"]', 1, '2024-10-01', 0, 6, 'Wang Wu', 603.30);
INSERT INTO `Reserve` VALUES (27, NULL, 2, '2024-09-28 11:15:32', 'luxibai@163.com', '2024-10-04', 3, '[\"bread\"]', 6, '2024-10-02', 1, 5, 'Li Si', 550.00);
INSERT INTO `Reserve` VALUES (28, NULL, 5, '2024-09-28 12:25:22', 'luxibai@163.com', '2024-10-05', 2, '[\"breakfast\"]', 5, '2024-10-03', 2, 3, 'Cao Jie', 825.00);
INSERT INTO `Reserve` VALUES (29, 5, 1, '2024-09-26 13:35:48', 'luxibai@163.com', '2024-10-06', 4, '[\"rice\"]', 9, '2024-10-04', 2, 4, 'Zhang San', 220.00);
INSERT INTO `Reserve` VALUES (30, NULL, 4, '2024-09-26 14:45:10', 'luxibai@163.com', '2024-10-07', 3, '[\"buns\"]', 7, '2024-10-05', 1, 5, 'Li Si', 1900.00);
INSERT INTO `Reserve` VALUES (31, NULL, 3, '2024-09-26 15:55:32', 'luxibai@163.com', '2024-10-08', 2, '[\"Chinese food\"]', 1, '2024-10-06', 0, 6, 'Wang Wu', 603.30);
INSERT INTO `Reserve` VALUES (32, NULL, 5, '2024-09-25 16:05:12', 'luxibai@163.com', '2024-10-09', 4, '[\"breakfast\"]', 8, '2024-10-07', 2, 4, 'Zhang San', 1200.00);
INSERT INTO `Reserve` VALUES (33, NULL, 2, '2024-09-25 17:15:32', 'luxibai@163.com', '2024-10-10', 3, '[\"rice\"]', 10, '2024-10-08', 1, 5, 'Li Si', 1020.00);
INSERT INTO `Reserve` VALUES (34, NULL, 1, '2024-09-24 18:25:22', 'luxibai@163.com', '2024-10-11', 2, '[\"buns\"]', 6, '2024-10-09', 0, 6, 'Wang Wu', 290.00);
INSERT INTO `Reserve` VALUES (35, NULL, 4, '2024-09-22 19:35:48', 'luxibai@163.com', '2024-10-12', 3, '[\"Chinese food\", \"breakfast\"]', 11, '2024-10-10', 1, 4, 'Zhang San', 1180.00);
INSERT INTO `Reserve` VALUES (36, NULL, 3, '2024-09-22 20:45:10', 'luxibai@163.com', '2024-10-13', 4, '[\"bread\"]', 7, '2024-10-11', 0, 5, 'Li Si', 800.00);
INSERT INTO `Reserve` VALUES (37, NULL, 2, '2024-09-19 21:55:32', 'luxibai@163.com', '2024-10-14', 2, '[\"buns\"]', 1, '2024-10-12', 1, 6, 'Wang Wu', 220.00);
INSERT INTO `Reserve` VALUES (38, NULL, 1, '2024-08-23 22:05:12', 'luxibai@163.com', '2024-10-15', 3, '[\"rice\", \"Chinese food\"]', 8, '2024-10-13', 2, 4, 'Zhang San', 1020.00);
INSERT INTO `Reserve` VALUES (39, NULL, 4, '2024-08-28 23:15:32', 'luxibai@163.com', '2024-10-16', 3, '[\"breakfast\"]', 5, '2024-10-14', 0, 5, 'Li Si', 510.00);
INSERT INTO `Reserve` VALUES (40, NULL, 5, '2024-08-24 00:25:22', 'luxibai@163.com', '2024-10-17', 4, '[\"Chinese food\"]', 6, '2024-10-15', 1, 6, 'Wang Wu', 725.00);
INSERT INTO `Reserve` VALUES (41, 7, 2, '2024-08-18 01:35:48', 'luxibai@163.com', '2024-10-18', 3, '[\"buns\"]', 7, '2024-10-16', 0, 4, 'Zhang San', 220.00);
INSERT INTO `Reserve` VALUES (42, NULL, 3, '2024-09-15 02:45:10', 'luxibai@163.com', '2024-10-19', 2, '[\"rice\", \"Chinese food\"]', 9, '2024-10-17', 2, 5, 'Li Si', 1320.00);
INSERT INTO `Reserve` VALUES (43, NULL, 4, '2024-10-18 03:55:32', 'luxibai@163.com', '2024-10-20', 3, '[\"breakfast\"]', 10, '2024-10-18', 1, 6, 'Wang Wu', 1100.00);
INSERT INTO `Reserve` VALUES (44, 4, 2, '2024-10-06 22:40:30', 'luxibai@163.com', '2024-10-07', 7, '[\"pasta\"]', 13, '2024-10-06', 0, 12, 'zhansan', 4400.00);
INSERT INTO `Reserve` VALUES (45, NULL, 1, '2024-10-06 22:41:29', 'luxibai@163.com', '2024-10-07', 7, '[\"bread\"]', 14, '2024-10-06', 2, 12, 'zhansan', 110.00);
INSERT INTO `Reserve` VALUES (46, NULL, 2, '2024-10-06 22:50:30', 'luxibai@163.com', '2024-10-08', 7, '[\"fried dough sticks\"]', 14, '2024-10-07', 1, 12, 'zhansan', 220.00);

-- ----------------------------
-- Table structure for Room
-- ----------------------------
DROP TABLE IF EXISTS `Room`;
CREATE TABLE `Room`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Room ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
  `hotel_id` int(11) NOT NULL COMMENT 'Hotel ID of the Room',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Room Image',
  `meal_plans_config` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Meal Plans Configuration',
  `price` decimal(10, 2) NOT NULL COMMENT 'Room Price',
  `room_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Room Name',
  `state` int(11) NOT NULL COMMENT 'Room Status: 0 is Available, 1 is Unavailable',
  `stock` int(11) NOT NULL COMMENT 'Room Stock',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Room Information Table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of Room
-- ----------------------------
INSERT INTO `Room` VALUES (1, '2024-10-05 03:17:07', 2, '/api/image/9ced0b49d3e2434988c0d3fc3bece64d.jpg', '{\"Breakfast\":[\"Bagel\"]}', 201.00, 'Single Room', 0, 98);
INSERT INTO `Room` VALUES (5, '2024-10-06 02:30:35', 2, '/api/image/cd646bf1fc2f4cab91a096163123addb.jpg', '{\"American\":[\"Rice\"],\"Breakfast\":[\"Bagel\"]}', 340.00, 'Double Room', 0, 102);
INSERT INTO `Room` VALUES (6, '2024-10-06 02:31:08', 3, '/api/image/db33905f0ee9487a94baa3cda65bbd4f.jpg', '{\"American\":[\"Rice\"],\"Breakfast\":[\"Bagel\"]}', 250.00, 'Single Room', 0, 100);
INSERT INTO `Room` VALUES (7, '2024-10-06 02:31:47', 3, '/api/image/52d56a3a18334dbcb5ae94f2c0b20a70.jpg', '{\"American\":[\"Rice\"],\"Breakfast\":[\"Bagel\"]}', 1900.00, 'Suite', 0, 50);
INSERT INTO `Room` VALUES (8, '2024-10-06 02:33:25', 4, '/api/image/e9b230b692524e2994edad0386595515.jpg', '{\"American\":[\"Rice\"],\"Breakfast\":[\"Bagel\"]}', 250.00, 'E-sports Room', 0, 100);
INSERT INTO `Room` VALUES (9, '2024-10-06 02:33:38', 4, '/api/image/d3f58f3159eb4a9a8f3da1b2a21b0a97.jpg', '{\"Breakfast\":[\"Bread\",\"Donut\",\"Pancake\"]}', 200.00, 'Single Room', 0, 202);
INSERT INTO `Room` VALUES (10, '2024-10-06 02:34:08', 5, '/api/image/cc59d4fbaa9343a4842910bac1023053.jpg', '{\"American\":[\"Rice\"],\"Breakfast\":[\"Bagel\"]}', 450.00, 'Double Room', 0, 100);
INSERT INTO `Room` VALUES (11, '2024-10-06 02:34:24', 5, '/api/image/c81b5c3a930543178dc03ee21fdd81f2.jpg', '{\"Breakfast\":[\"Bread\",\"Donut\",\"Pancake\"]}', 490.00, 'Theme Room', 0, 100);
INSERT INTO `Room` VALUES (12, '2024-10-06 02:34:50', 6, '/api/image/6fbd6912bff141c8ac6638e285ab8d0a.jpg', '{\"Breakfast\":[\"Bread\",\"Donut\",\"Pancake\"]}', 30.00, 'Single Room', 0, 100);
INSERT INTO `Room` VALUES (13, '2024-10-06 02:35:26', 7, '/api/image/361c8a02aef546a5a2e10d28faf8629d.jpg', '{\"Breakfast\":[\"Bread\",\"Donut\",\"Pancake\"]}', 2000.00, 'Luxury Room', 0, 98);
INSERT INTO `Room` VALUES (14, '2024-10-06 02:35:39', 7, '/api/image/a011853659b54cfda1322797edf8975d.jpg', '{\"Breakfast\":[\"Bread\",\"Donut\",\"Pancake\"]}', 100.00, 'Single Room', 0, 98);
INSERT INTO `Room` VALUES (15, '2024-10-06 02:36:12', 8, '/api/image/a19448410f144f7d9033b941cd583c10.jpg', '{\"American\":[\"Rice\"],\"Breakfast\":[\"Bagel\"]}', 250.00, 'Double Room', 0, 100);
INSERT INTO `Room` VALUES (16, '2024-10-06 02:36:51', 8, '/api/image/fa8f35a309024f3e927a532dc5652bf9.jpg', '{\"American\":[\"Rice\"],\"Breakfast\":[\"Bagel\"]}', 250.00, 'E-sports Room', 0, 100);
INSERT INTO `Room` VALUES (17, '2024-10-06 02:37:24', 9, '/api/image/8c0b72b6916b4c80865aff389c474c34.jpg', '{\"Breakfast\":[\"Bread\",\"Donut\",\"Pancake\"]}', 240.00, 'Single Room', 0, 100);
INSERT INTO `Room` VALUES (18, '2024-10-06 02:38:04', 9, '/api/image/7cd3e37d0e784cc6aa1ff106996beb6e.jpg', '{\"American\":[\"Kids Fries Set\",\"Buffet\"]}', 400.00, 'Theme Room', 0, 200);

-- ----------------------------
-- Table structure for User
-- ----------------------------
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ID',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Name',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Password',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Address',
  `age` int(11) NOT NULL COMMENT 'Age',
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Avatar',
  `business_registration_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Business Registration Number',
  `contact_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Contact Number',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Email',
  `id_card` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID Card',
  `role` int(11) NOT NULL COMMENT 'Role: 0 is Admin, 1 is Hotel Operator, 2 is Customer',
  `state` int(11) NOT NULL COMMENT 'Status: 0 is Normal, 1 is Banned',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Registration Time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of User
-- ----------------------------
INSERT INTO `User` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'New York, USA', 23, '/api/image/1cef0bb573ff44b7a4521265527e900b.png', '2024-112344', '13452324322', 'admin@qq.com', '70942718211110526X', 0, 0, '2024-10-03 07:48:27');
INSERT INTO `User` VALUES (2, 'LiaoNa', 'e10adc3949ba59abbe56e057f20f883e', 'Los Angeles, USA', 22, '/api/image/aa6129a07cb34112b5f82109ec7593e0.jpg', '017-06415323', '18681547681', 'luxibai@163.com', '708961201904204597', 1, 0, '2024-10-03 19:21:14');
INSERT INTO `User` VALUES (3, 'CaoJie', 'e10adc3949ba59abbe56e057f20f883e', 'Chicago, USA', 24, '/api/image/aa6129a07cb34112b5f82109ec7593e0.jpg', '0409-75645638', '18134277016', 'luxibai@163.com', '55804319971210142x', 2, 0, '2024-10-03 19:22:00');
INSERT INTO `User` VALUES (4, 'ZhangSan', 'e10adc3949ba59abbe56e057f20f883e', 'Houston, USA', 20, '/api/image/aa6129a07cb34112b5f82109ec7593e0.jpg', '2234-12223', '13451161222', 'luxibai@163.com', '12331241241242444', 2, 0, '2024-10-05 10:57:46');
INSERT INTO `User` VALUES (5, 'LiSi', 'e10adc3949ba59abbe56e057f20f883e', 'Phoenix, USA', 23, '/api/image/68087cae2cef44739edbecf02fd2fb51.jpg', '2234-1233', '13415552223', 'luxibai@163.com', '2334451231312333', 1, 0, '2024-10-05 11:45:50');
INSERT INTO `User` VALUES (6, 'WangWu', 'e10adc3949ba59abbe56e057f20f883e', 'Philadelphia, USA', 22, '/api/image/114c513d07de4af4ac745fe10202a0b4.jpg', '223-34567', '13452221233', 'luxibai@163.com', '4313434532414141', 2, 0, '2024-10-05 16:55:17');
INSERT INTO `User` VALUES (7, 'jack', 'e10adc3949ba59abbe56e057f20f883e', 'San Antonio, USA', 8, '/api/image/a1.jpg', '3242-123333', '13452223344', 'luxibai@163.com', '3441123213123233', 2, 0, '2024-10-06 19:28:58');
INSERT INTO `User` VALUES (8, 'tom', 'e10adc3949ba59abbe56e057f20f883e', 'San Diego, USA', 15, '/api/image/a1.jpg', '3241-4124123', '13451187889', 'luxibai@163.com', '1234141434141', 2, 0, '2024-10-06 20:32:15');

SET FOREIGN_KEY_CHECKS = 1;
