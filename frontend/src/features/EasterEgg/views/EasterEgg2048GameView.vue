<script setup>
import { ref, onMounted } from 'vue'

const size = 4
const board = ref([])
const score = ref(0)
const isGameOver = ref(false)

const generateEmptyBoard = () => {
  return Array.from({ length: size }, () => Array(size).fill(0))
}

const addRandomTile = () => {
  const empty = []
  for (let r = 0; r < size; r++) {
    for (let c = 0; c < size; c++) {
      if (board.value[r][c] === 0) empty.push([r, c])
    }
  }
  if (empty.length === 0) return
  const [r, c] = empty[Math.floor(Math.random() * empty.length)]
  board.value[r][c] = Math.random() < 0.9 ? 2 : 4
}

const resetGame = () => {
  board.value = generateEmptyBoard()
  score.value = 0
  isGameOver.value = false
  addRandomTile()
  addRandomTile()
}

const canMove = () => {
  // 빈 칸 있으면 무조건 이동 가능
  for (let r = 0; r < size; r++) {
    for (let c = 0; c < size; c++) {
      if (board.value[r][c] === 0) return true
    }
  }
  // 빈 칸 없으면 상하좌우 인접 타일 비교
  for (let r = 0; r < size; r++) {
    for (let c = 0; c < size; c++) {
      const current = board.value[r][c]
      if (r < size - 1 && board.value[r + 1][c] === current) return true
      if (c < size - 1 && board.value[r][c + 1] === current) return true
    }
  }
  return false
}

const handleKey = (e) => {
  if (isGameOver.value) return
  let moved = false
  switch (e.key) {
    case 'ArrowUp':
      moved = move('up')
      break
    case 'ArrowDown':
      moved = move('down')
      break
    case 'ArrowLeft':
      moved = move('left')
      break
    case 'ArrowRight':
      moved = move('right')
      break
  }
  if (moved) {
    addRandomTile()
    if (!canMove()) {
      isGameOver.value = true
    }
  }
}

const move = (direction) => {
  let moved = false
  const clone = board.value.map(row => row.slice())
  const merged = Array.from({ length: size }, () => Array(size).fill(false))

  const moveTile = (r, c, dr, dc) => {
    const val = clone[r][c]
    if (val === 0) return false
    let nr = r + dr
    let nc = c + dc
    while (nr >= 0 && nr < size && nc >= 0 && nc < size) {
      if (clone[nr][nc] === 0) {
        clone[nr][nc] = clone[nr - dr][nc - dc]
        clone[nr - dr][nc - dc] = 0
        r = nr
        c = nc
        nr += dr
        nc += dc
        moved = true
      } else if (clone[nr][nc] === val && !merged[nr][nc]) {
        clone[nr][nc] *= 2
        clone[r][c] = 0
        score.value += clone[nr][nc]
        merged[nr][nc] = true
        moved = true
        break
      } else {
        break
      }
    }
  }

  const range = [...Array(size).keys()]
  const ordered = direction === 'up' || direction === 'left' ? range : range.reverse()

  if (direction === 'up' || direction === 'down') {
    for (const c of range) {
      for (const r of ordered) {
        moveTile(r, c, direction === 'up' ? -1 : 1, 0)
      }
    }
  } else {
    for (const r of range) {
      for (const c of ordered) {
        moveTile(r, c, 0, direction === 'left' ? -1 : 1)
      }
    }
  }

  board.value = clone
  return moved
}

onMounted(() => {
  resetGame()
})
</script>

<template>
  <div
      class="game-container"
      @keydown.prevent="handleKey"
      tabindex="0"
  >
    <h1>2048 이스터에그 🎉</h1>
    <div class="grid">
      <div v-for="(row, rowIndex) in board" :key="rowIndex" class="row">
        <div
            v-for="(cell, colIndex) in row"
            :key="colIndex"
            class="cell"
            :class="'tile-' + cell"
        >
          {{ cell !== 0 ? cell : '' }}
        </div>
      </div>
    </div>
    <p>점수: {{ score }}</p>

    <button @click="resetGame">다시 시작</button>

    <div v-if="isGameOver" class="game-over-overlay">
      <div class="game-over-message">
        <h2>게임 오버 😢</h2>
        <button @click="resetGame">다시 시작</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.game-container {
  max-width: 400px;
  margin: 40px auto;
  text-align: center;
  user-select: none;
  outline: none;
  position: relative;
}

.grid {
  display: grid;
  grid-template-rows: repeat(4, 1fr);
  gap: 10px;
  background: #bbada0;
  padding: 10px;
  border-radius: 10px;
}

.row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.cell {
  background: #cdc1b4;
  height: 60px;
  line-height: 60px;
  font-size: 20px;
  font-weight: bold;
  color: #776e65;
  border-radius: 5px;
}

.tile-2 { background: #eee4da; }
.tile-4 { background: #ede0c8; }
.tile-8 { background: #f2b179; color: #f9f6f2; }
.tile-16 { background: #f59563; color: #f9f6f2; }
.tile-32 { background: #f67c5f; color: #f9f6f2; }
.tile-64 { background: #f65e3b; color: #f9f6f2; }
.tile-128 { background: #edcf72; color: #f9f6f2; }
.tile-256 { background: #edcc61; color: #f9f6f2; }
.tile-512 { background: #edc850; color: #f9f6f2; }
.tile-1024 { background: #edc53f; color: #f9f6f2; }
.tile-2048 { background: #edc22e; color: #f9f6f2; }

.game-over-overlay {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
}

.game-over-message {
  background: white;
  padding: 30px 40px;
  border-radius: 10px;
  text-align: center;
}
</style>
