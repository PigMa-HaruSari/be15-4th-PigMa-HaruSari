<!--
<template>
  <Header /> &lt;!&ndash; ✅ 헤더 추가 &ndash;&gt;
  <div class="category-main">
    <div class="category-header">
      <h2>카테고리 관리</h2>
      <button @click="showModal = true">+ 카테고리 추가</button>
    </div>

    <div v-if="categories.length === 0" class="empty">등록된 카테고리가 없습니다.</div>

    <div class="category-list">
      <div class="category-card"
           v-for="(category, index) in categories"
           :key="index"
           @click="CategoryActionModal(category)">
        <div class="card-header">
          <span class="icon">{{ category.icon || '📂' }}</span>
          <span class="title">{{ category.title }}</span>
        </div>
        <div class="card-footer">
          <span class="tag" :style="{ backgroundColor: category.color }"></span>
          <span class="status">{{ category.completed ? '✅ 완료' : '⏳ 진행 중' }}</span>
        </div>
      </div>
    </div>

    &lt;!&ndash; 카테고리 생성 모달 &ndash;&gt;
    <CategoryCreateModal v-if="showModal" @close="showModal = false" @created="handleCreated" />
  </div>
</template>

<script setup>
import CategoryCreateModal from '@/features/category/components/CategoryCreateModal.vue'
import { fetchCategory } from '@/features/main/mainApi'
import { createCategory } from '@/features/category/categoryApi.js'
import {onMounted, ref} from "vue";
import Header from "@/components/layout/Header.vue";
import CategoryActionModal from "@/features/category/components/CategoryActionModal.vue"; // 카테고리 생성 함수 추가

const showModal = ref(false)
const categories = ref([])
const selectedCategory = ref(null);
const showActionModal = ref(false);

// 카테고리 목록 조회
const loadCategories = async () => {
  try {
    const response = await fetchCategory()
    categories.value = response.data.data.map(category => ({
      title: category.categoryName,
      color: category.color,
      completed: category.completed // 선택적 필드
    }))
  } catch (e) {
    console.error('카테고리 불러오기 실패:', e)
  }
}

// 새 카테고리 생성 후 처리
const handleCreated = async (newCategory) => {
  try {
    // 백엔드에 카테고리 생성 요청
    const response = await createCategory({
      categoryName: newCategory.title,
      color: newCategory.color,
    })

    // 카테고리 생성 후 응답 처리
    if (response.status === 201) {
      categories.value.push({
        title: newCategory.title,
        color: newCategory.color,
        completed: false, // 기본값
      })
    }
    showModal.value = false
  } catch (error) {
    console.error('카테고리 생성 오류:', error)
    alert('카테고리 추가에 실패했습니다.')
  }
};

const openCategoryActionModal = (category) => {
  selectedCategory.value = category;
  showActionModal.value = true;
};

onMounted(loadCategories)
</script>

<style scoped>
.category-main {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #F8F7FF;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

h2 {
  color: #9381FF;
}

button {
  background-color: #9381FF;
  color: white;
  padding: 0.5rem 1.2rem;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
}

.category-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 1.2rem;
}

.category-card {
  background-color: white;
  border-radius: 12px;
  padding: 1rem;
  box-shadow: 0 4px 10px rgba(0,0,0,0.08);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.2rem;
  font-weight: 600;
}

.card-footer {
  margin-top: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
  color: #666;
}

.tag {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50%;
}

.empty {
  text-align: center;
  color: #aaa;
  font-size: 1rem;
  margin-top: 2rem;
}
</style>
-->

<template>
  <Header />

  <div class="category-main">
    <div class="category-header">
      <h2>카테고리 관리</h2>
      <button @click="showModal = true">+ 카테고리 추가</button>
    </div>

    <div v-if="categories.length === 0" class="empty">등록된 카테고리가 없습니다.</div>

    <div class="category-list">
      <div class="category-card"
           v-for="(category, index) in categories"
           :key="index"
           @click="openCategoryActionModal(category)">
        <div class="card-header">
          <span class="icon">{{ category.icon || '📂' }}</span>
          <span class="title">{{ category.title }}</span>
        </div>
        <div class="card-footer">
          <span class="tag" :style="{ backgroundColor: category.color }"></span>
          <span class="status">{{ category.completed ? '✅ 완료' : '⏳ 진행 중' }}</span>
        </div>
      </div>
    </div>

    <CategoryActionModal
        v-if="showActionModal"
        :category="selectedCategory"
        @close="showActionModal = false"
        @edit="handleEdit"
        @delete="handleDelete"
        @complete="handleComplete"
    />

    <CategoryCreateModal
        v-if="showModal"
        @close="showModal = false"
        @created="handleCreated"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Header from "@/components/layout/Header.vue";
import CategoryCreateModal from '@/features/category/components/CategoryCreateModal.vue';
import CategoryActionModal from "@/features/category/components/CategoryActionModal.vue";
import { fetchCategory } from '@/features/main/mainApi';
import { createCategory,completeCategory, deleteCategory, updateCategory, } from '@/features/category/categoryApi.js';

const showModal = ref(false);
const categories = ref([]);
const selectedCategory = ref(null);
const showActionModal = ref(false);
const isEdit = ref(false);
const editTarget = ref(null);

const loadCategories = async () => {
  try {
    const response = await fetchCategory();
    categories.value = response.data.data.map(category => ({
      categoryId: category.categoryId, // ✅ 추가!
      title: category.categoryName,
      color: category.color,
      completed: category.completed
    }));
  } catch (e) {
    console.error('카테고리 불러오기 실패:', e);
  }
};

const handleCreated = async (newCategory) => {
  try {
    const response = await createCategory({
      categoryName: newCategory.title,
      color: newCategory.color,
    });

    if (response.status === 201) {
      categories.value.push({
        title: newCategory.title,
        color: newCategory.color,
        completed: false,
      });
    }
    showModal.value = false;
  } catch (error) {
    console.error('카테고리 생성 오류:', error);
    alert('카테고리 추가에 실패했습니다.');
  }
};

const openCategoryActionModal = (category) => {
  selectedCategory.value = category;
  showActionModal.value = true;
};

const handleComplete = async (category) => {
  try {
    await completeCategory(category.categoryId);
    await loadCategories();
    showActionModal.value = false;
  } catch (e) {
    console.error('카테고리 완료 처리 실패:', e);
  }
};

const handleDelete = async (category) => {
  if (!confirm('정말 삭제하시겠습니까?')) return;
  try {
    await deleteCategory(category.categoryId, '카테고리를 삭제하겠습니다.', false);
    await loadCategories();
    showActionModal.value = false;
  } catch (e) {
    console.error('카테고리 삭제 실패:', e);
  }
};

const handleEdit = (category) => {
  isEdit.value = true;
  editTarget.value = category;
  showActionModal.value = false;
  showModal.value = true;
};

onMounted(loadCategories);
</script>

<style scoped>
.category-main {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #F8F7FF;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

h2 {
  color: #9381FF;
}

button {
  background-color: #9381FF;
  color: white;
  padding: 0.5rem 1.2rem;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
}

.category-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 1.2rem;
}

.category-card {
  background-color: white;
  border-radius: 12px;
  padding: 1rem;
  box-shadow: 0 4px 10px rgba(0,0,0,0.08);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.2rem;
  font-weight: 600;
}

.card-footer {
  margin-top: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
  color: #666;
}

.tag {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50%;
}

.empty {
  text-align: center;
  color: #aaa;
  font-size: 1rem;
  margin-top: 2rem;
}
</style>
