-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Час створення: Лис 13 2024 р., 10:04
-- Версія сервера: 8.0.40
-- Версія PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База даних: `bookstore`
--

-- --------------------------------------------------------

--
-- Структура таблиці `buyer`
--

CREATE TABLE `buyer` (
  `id` int NOT NULL,
  `storeName` varchar(255) NOT NULL,
  `fullName` varchar(255) NOT NULL,
  `phoneNumber` varchar(20) DEFAULT NULL,
  `purchaseId` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Дамп даних таблиці `buyer`
--

INSERT INTO `buyer` (`id`, `storeName`, `fullName`, `phoneNumber`, `purchaseId`) VALUES
(3, 'BookHouse', 'Іван Петров', '+380501234567', 2),
(4, 'ReadersHub', 'Олена Іванова', '+380931234567', 3),
(6, 'BookWorld', 'Петро Сидоренко', '+380671234567', 4);

-- --------------------------------------------------------

--
-- Структура таблиці `purchase`
--

CREATE TABLE `purchase` (
  `purchaseId` int NOT NULL,
  `bookTitle` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `cost` decimal(10,2) NOT NULL,
  `genre` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Дамп даних таблиці `purchase`
--

INSERT INTO `purchase` (`purchaseId`, `bookTitle`, `quantity`, `cost`, `genre`) VALUES
(2, 'Убити пересмішника', 2, 19.99, 'Класична література'),
(3, '1984', 1, 15.50, 'Антиутопія'),
(4, 'Великий Гетсбі', 3, 25.00, 'Класична література');

-- --------------------------------------------------------

--
-- Структура таблиці `sellers`
--

CREATE TABLE `sellers` (
  `id` int NOT NULL,
  `store_name` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `shift` varchar(50) DEFAULT NULL,
  `track_number` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Дамп даних таблиці `sellers`
--

INSERT INTO `sellers` (`id`, `store_name`, `full_name`, `address`, `phone`, `shift`, `track_number`) VALUES
(2, 'Магазин \"Літера\"', 'Марія Коваль', 'вул. Гоголя, 7, Харків', '+380681234567', 'Нічна', 'TN001'),
(3, 'Книжкова лавка \"Ранок\"', 'Олександр Іванов', 'вул. Ватутіна, 45, Одеса\'', '+380931234567', 'Денна', 'TN002');

--
-- Індекси збережених таблиць
--

--
-- Індекси таблиці `buyer`
--
ALTER TABLE `buyer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `purchaseId` (`purchaseId`);

--
-- Індекси таблиці `purchase`
--
ALTER TABLE `purchase`
  ADD PRIMARY KEY (`purchaseId`);

--
-- Індекси таблиці `sellers`
--
ALTER TABLE `sellers`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для збережених таблиць
--

--
-- AUTO_INCREMENT для таблиці `buyer`
--
ALTER TABLE `buyer`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT для таблиці `purchase`
--
ALTER TABLE `purchase`
  MODIFY `purchaseId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT для таблиці `sellers`
--
ALTER TABLE `sellers`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Обмеження зовнішнього ключа збережених таблиць
--

--
-- Обмеження зовнішнього ключа таблиці `buyer`
--
ALTER TABLE `buyer`
  ADD CONSTRAINT `buyer_ibfk_1` FOREIGN KEY (`purchaseId`) REFERENCES `purchase` (`purchaseId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
