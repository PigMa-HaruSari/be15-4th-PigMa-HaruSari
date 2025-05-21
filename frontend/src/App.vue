<script setup>
import HeaderView from './components/layout/Header.vue'
</script>

<template>
  <HeaderView />
  <router-view />
</template>

<style>
html, body {
  padding: 0 !important;
  margin: 0 !important;
  box-sizing: border-box;
}
</style>
