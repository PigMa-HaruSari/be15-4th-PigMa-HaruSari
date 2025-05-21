<template>
  <header class="header">
    <div class="header-inner">
      <div class="logo">
        <img src="@/assets/images/HARURAMENSARI.png" alt="하루살이 로고" />
        HARUSARI
      </div>
      <nav class="nav">
        <button>카테고리</button>
        <button>통계</button>
        <button>마이페이지</button>
      </nav>
    </div>
  </header>
</template>

<script setup>

</script>

<style scoped>


.header {
  width: 100vw;
  height: 100px;
  background-color: #b8b8ff;
  display: flex;
  justify-content: center;
  align-items: center;
}
.header-inner {
  width: 100%;
  max-width: 1200px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0; /* padding 제거 */
}
.logo {
  font-size: 30px;
  font-weight: bold;
  color: #ffffff;
  display: flex;
  align-items: center;
  gap: 10px;
}
.logo img {
  height: 60px;
}
.nav {
  display: flex;
  gap: 20px;
}
.nav button {
  background: none;
  border: none;
  font-size: 16px;
  color: #ffffff;
  cursor: pointer;
}
</style>
