# 🧩 ATP-Maze

**ATP-Maze** is a Java-based project that allows you to generate, solve, and visualize mazes 🏞️. With advanced algorithms, JavaFX GUI, and client-server communication, you can explore various maze-solving techniques! 🧠

## ✨ Features
- 🔄 Generate 2D & 3D mazes
- 🔍 Solve using BFS, DFS, Best-First Search
- 🚀 Multithreading for efficiency
- 🌐 Client-server interaction
- 🖼️ Visualize mazes with JavaFX GUI
- 📦 Compression for fast data transfer

## 🚀 Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/shaySitri/ATP-Maze.git
   ```
2. Compile:
   ```bash
   javac -cp src src/Main.java
   ```
3. Run:
   ```bash
   java -cp src Main
   ```

## 🎮 How to Play
1. **Generate a Maze**: Pick your maze size and algorithm to create 🏗️.
2. **Solve the Maze**: Select a solving algorithm and watch it find the path 🎯.
3. **Watch**: Visualize the solution unfold on the GUI 🖥️.
4. **Client-Server**: Request mazes from a server, solve remotely 🌍.

## 🛠️ Usage
1. **Maze Generation**: Choose your algorithm (e.g., `MyMazeGenerator`), input size (50x50), and hit generate.
2. **Maze Solving**: Select a solving method and let the magic happen! ✨

## 📦 Compression
Efficient maze compression ensures faster transmission over networks. Use `MyCompressorOutputStream` and `MyDecompressorInputStream` to handle maze data 🗃️.
