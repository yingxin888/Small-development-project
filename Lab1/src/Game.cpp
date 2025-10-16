#include "../include/Game.h"

Game::Game() = default;

void Game::initGame() {
    gameBoard.reset();
    currentScore = 0;
    moveCount = 0;
    historyGameBoards = std::stack<GameBoard>();
    historyScores = std::stack<int>();
    startTime = std::chrono::system_clock::now();
    historyGameBoards.push(gameBoard);
    historyScores.push(currentScore); // Save initial state
}

void Game::updateGame(Direction direction) {
    // Save current state to history
    GameBoard prevBoard = gameBoard;
    int prevScore = currentScore;
    historyGameBoards.push(prevBoard);
    historyScores.push(prevScore);
    
    // 执行移动
    int moveScore = gameBoard.move(direction);
    
    // 只有移动有效（分数变化或棋盘变化）时才保存历史
    if (moveScore > 0) {
        currentScore += moveScore;
        moveCount++;
    }
    else{
        moveCount++;
    }
    // TODO 3

}

void Game::undoLastMove() {
    if (historyGameBoards.size() > 1 && historyScores.size() > 1) {
        // Remove current state
        gameBoard = historyGameBoards.top();
        historyGameBoards.pop();
        // TODO 1
        currentScore = historyScores.top();
        historyScores.pop();
        moveCount--;
        // Restore previous state
        // TODO 2

    } 
    else {
        currentScore = 0;
        moveCount = 0;
    }
}

bool Game::hasWon() const {
    return gameBoard.hasWinningTile();
}

bool Game::hasLost() const {
    return gameBoard.isGameOver();
}

double Game::getElapsedTime() const {
    auto endTime = std::chrono::system_clock::now();
    std::chrono::duration<double> elapsedSeconds = endTime - startTime;
    return elapsedSeconds.count();
}

int Game::getScore() const {
    return currentScore;
}

int Game::getMoveCount() const {
    return moveCount;
}

const GameBoard& Game::getGameBoard() const {
    return gameBoard;
}