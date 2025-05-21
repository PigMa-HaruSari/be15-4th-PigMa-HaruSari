  <template>
    <div>
      <Header/>
      <div class="main-wrapper">
        <div class="main">
          <!-- 달력 영역 -->
          <div class="calendar-content">
            <div class="calendar-box">
              <div id="calendar"></div>
            </div>
          </div>

          <!-- 할 일 + 회고 영역 -->
          <div class="right-content">
            <div class="today-task-box">
              <div class="today-task-header">
                <h4>오늘 할 일</h4>
                <button class="add-task-btn">할 일 추가</button>
              </div>
              <!-- 카테고리별 할 일 리스트 -->
              <div class="category" v-for="(category, index) in categories" :key="index">
                <div class="category-title">
                  {{ category.icon }} {{ category.title }}
                  <span :class="['category-tag', category.tagClass]">O</span>
                </div>
                <div
                    class="category-task"
                    v-for="(task, i) in category.tasks"
                    :key="i"
                    :class="{ [category.completedClass]: task.completed }"
                >
                  <input type="checkbox" v-model="task.completed" />
                  {{ task.text }}
                </div>
              </div>
            </div>

            <!-- 회고 영역 -->
            <div class="review-box">
              <h4>회고</h4>
              <textarea v-model="reviewText" placeholder="오늘의 회고를 작성해보세요..."></textarea>
              <div class="review-actions">
                <button @click="reviewText = ''">회고 삭제</button>
                <button @click="saveReview">회고 저장</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>

  <script setup>
  import { onMounted, ref } from 'vue';
  import Header from '@/components/layout/Header.vue';
  import { Calendar } from '@fullcalendar/core';
  import dayGridPlugin from '@fullcalendar/daygrid';
  import koLocale from '@fullcalendar/core/locales/ko';

  const reviewText = ref('');

  const categories = ref([
    {
      icon: '📚',
      title: '스프링 부트 공부',
      tagClass: 'tag-purple',
      completedClass: 'completed-purple',
      tasks: [
        { text: '인프런 섹션 1강 듣고 정리하기', completed: false },
        { text: 'JPA 엔티티 매핑 실습하기', completed: false },
        { text: 'Postman으로 API 테스트 해보기', completed: false },
        { text: 'Swagger 문서화 연습하기', completed: false }
      ]
    },
    {
      icon: '🏃',
      title: '운동',
      tagClass: 'tag-green',
      completedClass: 'completed-green',
      tasks: [
        { text: '스트레칭 10분 (목/어깨 위주)', completed: false },
        { text: '만보 걷기 or 5km 산책', completed: false },
        { text: '유튜브 홈트 20분 따라하기', completed: false }
      ]
    },
    {
      icon: '🤖',
      title: '취업',
      tagClass: 'tag-yellow',
      completedClass: 'completed-yellow',
      tasks: [
        { text: '자기소개서 작성하기', completed: false },
        { text: '모의 면접 진행하기', completed: false },
        { text: '이력서 장성하기', completed: false }
      ]
    }
  ]);

  function saveReview() {
    alert('회고가 저장되었습니다.');
  }

  onMounted(() => {
    const calendarEl = document.getElementById('calendar');
    const calendar = new Calendar(calendarEl, {
      plugins: [dayGridPlugin],
      initialView: 'dayGridMonth',
      height: '100%',
      locale: koLocale,
      timeZone: 'local'
    });
    calendar.render();
  });
  </script>

  <style scoped>
  .main-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    background: #F8F7FF;
    padding: 40px 0;
    min-height: calc(100vh - 80px);
  }
  .main {
    width: 1200px;
    display: flex;
    gap: 32px;
  }
  .calendar-content {
    width: 480px;
    height: 560px;
    background: #FFFFFF;
    border-radius: 16px;
    padding: 25px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    display: flex;
    flex-direction: column;
  }
  .calendar-box {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  #calendar {
    width: 100%;
    height: 100%;
  }
  .right-content {
    width: 648px;
    display: flex;
    flex-direction: column;
    gap: 24px;
    height: 600px;
    overflow-y: auto;
    box-sizing: border-box;
    padding: 0;
  }
  .today-task-box,
  .review-box {
    background: #FFFFFF;
    padding: 20px;
    border-radius: 16px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    flex-shrink: 0;
  }
  .today-task-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
  }
  .today-task-header h4 {
    font-size: 20px;
    font-weight: 600;
    color: #333;
  }
  .add-task-btn {
    font-size: 14px;
    padding: 6px 12px;
    background-color: #9381FF;
    color: #fff;
    border: none;
    border-radius: 8px;
    cursor: pointer;
  }
  .category {
    margin-bottom: 16px;
    border-radius: 12px;
    padding: 16px;
    background-color: #F8F7FF;
  }
  .category-title {
    font-weight: bold;
    font-size: 18px;
    margin-bottom: 12px;
    color: #333;
    display: flex;
    align-items: center;
    gap: 10px;
  }
  .category-tag {
    display: inline-block;
    padding: 2px 8px;
    font-size: 12px;
    border-radius: 8px;
    color: #333;
  }
  .tag-purple {
    background-color: #CDB4DB;
  }
  .tag-green {
    background-color: #B5EAD7;
  }
  .tag-yellow {
    background-color: #FFF1A8;
  }
  .category-task {
    display: flex;
    align-items: center;
    font-size: 16px;
    padding: 8px 0;
    border-bottom: 1px solid #ccc;
  }
  .category-task:last-child {
    border-bottom: none;
  }
  .category-task input[type='checkbox'] {
    margin-right: 10px;
  }
  .completed-purple {
    background-color: #CDB4DB;
    border-radius: 8px;
  }
  .completed-green {
    background-color: #B5EAD7;
    border-radius: 8px;
  }
  .completed-yellow {
    background-color: #FFF1A8;
    border-radius: 8px;
  }
  .review-box textarea {
    width: 100%;
    height: 120px;
    border: 1px solid #ccc;
    border-radius: 6px;
    padding: 10px;
    resize: none;
    margin-top: 12px;
  }
  .review-actions {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 10px;
  }
  .review-actions button {
    padding: 8px 16px;
    font-size: 14px;
    border: none;
    border-radius: 6px;
    cursor: pointer;
  }
  .review-actions button:first-child {
    background-color: #FFEEDD;
    color: #333;
  }
  .review-actions button:last-child {
    background-color: #FFD8BE;
    color: #333;
  }
  </style>
