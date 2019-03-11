-- phpMyAdmin SQL Dump
-- version 4.8.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 13, 2018 at 06:03 AM
-- Server version: 10.1.31-MariaDB
-- PHP Version: 5.6.35

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `e_learning`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_materi`
--

CREATE TABLE `tb_materi` (
  `no_id` int(11) NOT NULL,
  `nameUrl` varchar(100) DEFAULT NULL,
  `video` varchar(100) DEFAULT NULL,
  `nameUrl2` varchar(100) DEFAULT NULL,
  `video2` varchar(100) DEFAULT NULL,
  `linkKuis` varchar(100) DEFAULT NULL,
  `linkNilai` varchar(100) DEFAULT NULL,
  `create_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_materi`
--

INSERT INTO `tb_materi` (`no_id`, `nameUrl`, `video`, `nameUrl2`, `video2`, `linkKuis`, `linkNilai`, `create_date`) VALUES
(3, 'PTI', 'http://192.168.43.44/training/directoryVideos/VID-20180831-WA0026.mp4', 'PTI Slide 1', 'http://192.168.43.44/training/directoryVideos/VID-20180831-WA0026.mp4', 'https://www.google.com/search?safe=strict&ei=Yr6YW6-ULoTpvASJx6fYDw&q=digital+signage+bengkel+motor&', 'null', '2018-09-17'),
(4, 'Indonesia', 'http://192.168.43.44/training/directoryVideos/VID-20180808-WA0004.mp4', 'Indonesia Slide 1', 'http://192.168.43.44/training/directoryVideos/VID-20180806-WA0021.mp4', 'null', 'null', '2018-09-17'),
(5, 'djdjdjjs', 'http://192.168.43.44/training/directoryVideos/VID-20180916-WA0003.mp4', 'dneiwowkwm', 'http://192.168.43.44/training/directoryVideos/VID-20180913-WA0008.mp4', 'susjsjshs', 'null', '2018-09-20'),
(6, 'jsisisiwkwjwjw', 'http://192.168.43.44/training/directoryVideos/mobizen_20180909_204746.mp4', 'jaiaianabhzz', 'http://192.168.43.44/training/directoryVideos/trendinginfo.id_BnP7cW7DXv1.mp4', 'wiiwiwiwiw', 'null', '2018-09-20');

-- --------------------------------------------------------

--
-- Table structure for table `tb_users`
--

CREATE TABLE `tb_users` (
  `id` int(15) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `level` enum('dosen','mahasiswa') NOT NULL,
  `foto` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_users`
--

INSERT INTO `tb_users` (`id`, `username`, `email`, `password`, `level`, `foto`) VALUES
(40, 'Jakadd', 'sdasda', 'udin', 'dosen', 'http://192.168.43.44/training/directoryPhoto/40.png'),
(41, 'iqbal', 'iqbalabdulmalik21@gmail.com', '21031994', 'mahasiswa', 'http://192.168.43.44/training/directoryPhoto/41.png'),
(42, 'Udin', 'Yyyy', '77778', 'mahasiswa', 'http://192.168.43.44/training/directoryPhoto/42.png'),
(43, 'Kemal', 'kemallll', '543', 'mahasiswa', 'http://192.168.43.44/training/directoryPhoto/43.png'),
(45, '$username', '$email', '$password', 'mahasiswa', '0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_materi`
--
ALTER TABLE `tb_materi`
  ADD PRIMARY KEY (`no_id`);

--
-- Indexes for table `tb_users`
--
ALTER TABLE `tb_users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_materi`
--
ALTER TABLE `tb_materi`
  MODIFY `no_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `tb_users`
--
ALTER TABLE `tb_users`
  MODIFY `id` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
