CREATE TABLE `books` (
  `id` bigint(20) AUTO_INCREMENT,
  `author` varchar(255),
  `launch_date` date NOT NULL,
  `price` decimal(65,2) NOT NULL,
  `title` varchar(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
