-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Час створення: Чрв 23 2026 р., 19:20
-- Версія сервера: 8.0.41
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
(1, 'BookHouse', 'Іван Петренко', '+380501112233', 1),
(2, 'ReadersHub', 'Олена Коваль', '+380671234567', 2),
(3, 'BookWorld', 'Михайло Сидоренко', '+380931112233', 3),
(4, 'Книжкова Планета', 'Анна Іванчук', '+380991234567', 4),
(5, 'Світ Книг', 'Василь Бондар', '+380731234567', 5);

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
(1, '1984', 2, 15.50, 'Класична література'),
(2, 'Майстер і Маргарита', 1, 18.90, 'Роман'),
(3, 'Тіні забутих предків', 3, 12.50, 'Класика'),
(4, 'Кобзар', 2, 20.00, 'Поезія'),
(5, 'Гаррі Поттер і філософський камінь', 4, 25.00, 'Фентезі');

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
(1, 'BookHouse', 'Марія Коваль', 'вул. Шевченка, 10, Львів', '+380501111111', 'Денна', 'TN001'),
(2, 'ReadersHub', 'Олександр Мельник', 'вул. Франка, 15, Київ', '+380671111111', 'Нічна', 'TN002'),
(3, 'BookWorld', 'Ірина Бойко', 'просп. Соборний, 50, Запоріжжя', '+380931111111', 'Денна', 'TN003'),
(4, 'Книжкова Планета', 'Наталія Ткаченко', 'вул. Грушевського, 22, Тернопіль', '+380991111111', 'Вечірня', 'TN004'),
(5, 'Світ Книг', 'Андрій Савчук', 'вул. Незалежності, 5, Івано-Франківськ', '+380731111111', 'Денна', 'TN005');

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
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT для таблиці `purchase`
--
ALTER TABLE `purchase`
  MODIFY `purchaseId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT для таблиці `sellers`
--
ALTER TABLE `sellers`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
