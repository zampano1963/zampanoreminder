# Zampano Reminder - Task List

> plan.md 기반 세부 작업 체크리스트. 완료 시 `[x]`로 체크.

---

## Phase 1: 기본 리마인더 CRUD + Next.js 셋업

### Backend

- [x] **1-B1** Reminder 엔티티 확장
  - [x] Priority enum 생성 (NONE, LOW, MEDIUM, HIGH)
  - [x] Reminder에 notes(String), url(String), priority(Priority), flagged(boolean), sortOrder(int) 필드 추가
  - [x] application.yml의 ddl-auto로 스키마 자동 반영 확인

- [x] **1-B2** DTO 생성
  - [x] ReminderRequest DTO (생성/수정 요청용)
  - [x] ReminderResponse DTO (응답용)
  - [x] Entity ↔ DTO 변환 로직 (서비스 또는 매퍼)

- [x] **1-B3** ReminderController 확장
  - [x] `PUT /api/reminders/{id}` - 리마인더 수정
  - [x] `PATCH /api/reminders/{id}/toggle` - 완료 토글
  - [x] 기존 POST/GET/DELETE를 DTO 기반으로 변경

- [x] **1-B4** ReminderService 확장
  - [x] update(Long id, ReminderRequest) 메서드
  - [x] toggleComplete(Long id) 메서드
  - [x] 존재하지 않는 리마인더 처리 (예외)

- [x] **1-B5** CORS 설정
  - [x] WebMvcConfigurer 구현하여 `http://localhost:3000` 허용
  - [x] 허용 메서드: GET, POST, PUT, PATCH, DELETE, OPTIONS

- [x] **1-B6** 글로벌 에러 핸들러
  - [x] @RestControllerAdvice 클래스 생성
  - [x] ErrorResponse DTO (error, code 필드)
  - [x] RuntimeException, MethodArgumentNotValidException 등 처리

- [x] **1-B7** DataInitializer 확장
  - [x] 다양한 priority, flagged, 날짜의 샘플 리마인더 5~10개 생성

- [x] **1-B 검증** Backend 빌드 및 API 테스트
  - [x] `./gradlew build` 성공
  - [x] `./gradlew bootRun` 후 curl로 CRUD + toggle 동작 확인

### Frontend

- [x] **1-F1** Next.js 프로젝트 초기화
  - [x] `frontend/` 디렉토리에 create-next-app (TypeScript, Tailwind CSS, App Router)
  - [x] 불필요한 보일러플레이트 정리
  - [x] `npm run dev`로 정상 실행 확인 (localhost:3000)

- [x] **1-F2** API client 모듈
  - [x] `lib/api.ts` 생성: fetch 래퍼 함수
  - [x] BASE_URL 환경변수 설정 (`NEXT_PUBLIC_API_URL=http://localhost:9090`)
  - [x] GET, POST, PUT, PATCH, DELETE 헬퍼 함수
  - [x] JSON 파싱 및 에러 처리

- [x] **1-F3** TypeScript 타입 정의
  - [x] `types/reminder.ts`: Reminder 인터페이스, Priority enum
  - [x] ReminderRequest, ReminderResponse 타입

- [x] **1-F4** 기본 레이아웃
  - [x] `components/layout/AppLayout.tsx`: 2-column (사이드바 240px + 메인)
  - [x] `components/layout/Sidebar.tsx`: placeholder (Phase 2에서 완성)
  - [x] `components/layout/Header.tsx`: 앱 제목 + 검색 placeholder
  - [x] `app/layout.tsx`에 AppLayout 적용

- [x] **1-F5** 리마인더 목록 컴포넌트
  - [x] `components/reminder/ReminderList.tsx`: API에서 리마인더 조회 및 렌더링
  - [x] `components/reminder/ReminderItem.tsx`: 개별 리마인더 행 (체크박스 + 제목 + 날짜)
  - [x] 빈 목록 시 안내 텍스트

- [x] **1-F6** 리마인더 생성
  - [x] `components/reminder/AddReminder.tsx`: 하단 인라인 입력 ("+ 새로운 미리알림")
  - [x] 클릭 시 입력 필드 활성화, Enter로 생성, Escape로 취소
  - [x] 생성 후 목록에 즉시 반영

- [x] **1-F7** 리마인더 완료 토글
  - [x] 원형 체크박스 컴포넌트
  - [x] 클릭 시 PATCH toggle API 호출
  - [x] 완료 시 텍스트에 취소선 + 흐려지는 효과

- [x] **1-F8** 리마인더 삭제
  - [x] 삭제 버튼 (호버 시 표시) 또는 우클릭 메뉴
  - [x] 삭제 전 확인 다이얼로그
  - [x] DELETE API 호출 후 목록에서 제거

- [x] **1-F9** 리마인더 상세 편집 패널
  - [x] `components/reminder/ReminderDetail.tsx`: 우측 슬라이드 패널
  - [x] 제목, 메모, URL, 날짜/시간, 우선순위 편집 필드
  - [x] 변경 시 PUT API 호출 (debounce 또는 blur 시 저장)
  - [x] 패널 닫기 버튼

- [x] **1-F10** Apple 스타일 기초 CSS
  - [x] 시스템 폰트 스택 설정 (-apple-system, SF Pro 등)
  - [x] 12색 팔레트 CSS 변수 정의
  - [x] 라운드 카드, 그림자, 기본 간격 변수
  - [x] globals.css에 기본 스타일 적용

- [ ] **1-F 검증** Frontend 통합 테스트 (브라우저 수동 확인 필요)
  - [x] Backend + Frontend 동시 실행
  - [ ] 브라우저에서 리마인더 생성 → 목록 표시 확인
  - [ ] 리마인더 수정 → 반영 확인
  - [ ] 리마인더 완료 토글 → 상태 변경 확인
  - [ ] 리마인더 삭제 → 목록에서 제거 확인

---

## Phase 2: 리스트(카테고리) + 스마트 리스트

### Backend

- [ ] **2-B1** ReminderList 엔티티
  - [ ] id, name, color, icon, sortOrder 필드
  - [ ] JPA 어노테이션 및 Lombok 설정

- [ ] **2-B2** Reminder ↔ ReminderList 연관관계
  - [ ] Reminder에 `@ManyToOne` listId(FK) 추가
  - [ ] ReminderList에 `@OneToMany` reminders 추가

- [ ] **2-B3** ReminderListController
  - [ ] `GET /api/lists` - 전체 리스트 조회 (미완료 카운트 포함)
  - [ ] `POST /api/lists` - 리스트 생성
  - [ ] `PUT /api/lists/{id}` - 리스트 수정
  - [ ] `DELETE /api/lists/{id}` - 리스트 삭제 (포함된 리마인더도 삭제)

- [ ] **2-B4** 리스트별 리마인더 조회
  - [ ] `GET /api/lists/{id}/reminders`

- [ ] **2-B5** 스마트 리스트 API
  - [ ] `GET /api/reminders/smart/today` - 오늘 리마인더
  - [ ] `GET /api/reminders/smart/scheduled` - 날짜 설정된 미래 리마인더
  - [ ] `GET /api/reminders/smart/all` - 전체 미완료 리마인더
  - [ ] `GET /api/reminders/smart/flagged` - 플래그된 리마인더
  - [ ] `GET /api/reminders/smart/completed` - 완료된 리마인더

- [ ] **2-B6** 스마트 리스트 카운트 API
  - [ ] `GET /api/reminders/smart/counts` - {today, scheduled, all, flagged, completed} 카운트

- [ ] **2-B7** 기본 리스트 자동 생성
  - [ ] DataInitializer에서 "미리알림" 기본 리스트 생성
  - [ ] 기본 리스트는 삭제 불가 로직 추가

- [ ] **2-B 검증** Backend API 테스트
  - [ ] 리스트 CRUD curl 테스트
  - [ ] 스마트 리스트 각 타입별 조회 확인
  - [ ] 카운트 API 정확성 확인

### Frontend

- [ ] **2-F1** 스마트 리스트 카드 그리드
  - [ ] 사이드바 상단: 오늘/예정/전체/플래그/완료됨 카드
  - [ ] 각 카드에 아이콘 + 카운트 표시
  - [ ] 카드별 고유 색상 (Apple 스타일)

- [ ] **2-F2** 나의 목록 사이드바
  - [ ] 사이드바 중간: 사용자 리스트 목록
  - [ ] 색상 도트 + 리스트 이름 + 미완료 카운트
  - [ ] "목록 추가" 버튼

- [ ] **2-F3** 리스트 생성 다이얼로그
  - [ ] 모달: 이름 입력 + 12색 팔레트 선택 + 아이콘 선택
  - [ ] 생성 후 사이드바에 즉시 반영

- [ ] **2-F4** 리스트 수정/삭제
  - [ ] 리스트 우클릭 또는 편집 버튼으로 수정 다이얼로그
  - [ ] 리스트 삭제 시 확인 다이얼로그 (포함된 리마인더 삭제 경고)

- [ ] **2-F5** 리스트 클릭 시 필터링
  - [ ] 사이드바 리스트 클릭 → 메인 영역에 해당 리스트 리마인더만 표시
  - [ ] 선택된 리스트 하이라이트

- [ ] **2-F6** 스마트 리스트 클릭 시 필터링
  - [ ] 오늘/예정/전체/플래그/완료됨 각각 올바른 데이터 표시

- [ ] **2-F7** 예정 스마트 리스트: 날짜별 그룹핑
  - [ ] 날짜 섹션 헤더 + 해당 날짜의 리마인더 목록

- [ ] **2-F8** 전체 스마트 리스트: 리스트별 그룹핑
  - [ ] 리스트 이름 섹션 헤더 + 해당 리스트의 리마인더 목록

- [ ] **2-F9** 리마인더 생성 시 리스트 자동 배정
  - [ ] 현재 선택된 리스트에서 리마인더 추가 시 해당 리스트에 배정

- [ ] **2-F10** TanStack Query 도입
  - [ ] `npm install @tanstack/react-query`
  - [ ] QueryClientProvider 설정
  - [ ] 리마인더/리스트 API를 useQuery/useMutation으로 전환
  - [ ] 캐시 무효화 전략 설정

- [ ] **2-F 검증** 리스트 + 스마트 리스트 통합 테스트
  - [ ] 리스트 생성 → 색상/아이콘 설정 → 사이드바 반영 확인
  - [ ] 리스트 클릭 → 해당 리마인더만 표시 확인
  - [ ] 5개 스마트 리스트 각각 올바른 필터링 확인
  - [ ] 카운트 정확성 확인

---

## Phase 3: 태그 + 검색 + 우선순위/플래그 UI

### Backend

- [ ] **3-B1** Tag 엔티티 + 다대다 매핑
  - [ ] Tag: id, name 필드
  - [ ] reminder_tags 조인 테이블 (reminder_id, tag_id)
  - [ ] Reminder에 `@ManyToMany` tags 추가

- [ ] **3-B2** Tag CRUD API
  - [ ] `GET /api/tags` - 전체 태그 조회
  - [ ] `POST /api/tags` - 태그 생성
  - [ ] `DELETE /api/tags/{id}` - 태그 삭제

- [ ] **3-B3** 태그 필터 API
  - [ ] `GET /api/reminders?tags=tag1,tag2` - AND 조건 필터링
  - [ ] Repository에 태그 기반 쿼리 메서드 추가

- [ ] **3-B4** 검색 API
  - [ ] `GET /api/reminders/search?q={keyword}`
  - [ ] title, notes에서 LIKE 검색 (대소문자 무시)

- [ ] **3-B 검증** 태그/검색 API 테스트
  - [ ] 태그 생성 → 리마인더에 태그 연결 → 필터링 확인
  - [ ] 검색 API 키워드 매칭 확인

### Frontend

- [ ] **3-F1** 우선순위 표시
  - [ ] ReminderItem에 priority별 마크 표시 (! / !! / !!!)
  - [ ] 마크 색상: 높음=빨강, 보통=주황, 낮음=파랑

- [ ] **3-F2** 우선순위 선택 UI
  - [ ] 상세 편집 패널에 4단계 선택 드롭다운 또는 세그먼트

- [ ] **3-F3** 플래그 토글
  - [ ] ReminderItem 우측에 플래그 아이콘
  - [ ] 클릭 시 토글 (주황색 ↔ 회색)
  - [ ] PATCH API 호출

- [ ] **3-F4** 태그 입력
  - [ ] 상세 편집 패널에 태그 입력 필드
  - [ ] `#` 입력 시 기존 태그 자동완성 드롭다운
  - [ ] 새 태그 생성 지원
  - [ ] 태그 칩으로 표시, X 버튼으로 제거

- [ ] **3-F5** 태그 브라우저
  - [ ] 사이드바 하단에 "태그" 섹션
  - [ ] 모든 태그 목록 표시

- [ ] **3-F6** 태그 필터링
  - [ ] 태그 클릭 시 해당 태그의 리마인더만 메인 영역에 표시
  - [ ] 복수 태그 선택 시 AND 조건

- [ ] **3-F7** 검색바
  - [ ] 헤더에 검색 입력 필드
  - [ ] 입력 시 debounce 300ms 후 검색 API 호출
  - [ ] 입력 중 로딩 인디케이터

- [ ] **3-F8** 검색 결과 표시
  - [ ] 리스트별 그룹핑
  - [ ] 매칭 텍스트 하이라이트 (볼드 또는 배경색)

- [ ] **3-F9** Optimistic Update
  - [ ] 완료 토글: 클릭 즉시 UI 반영, API 실패 시 롤백
  - [ ] 플래그 토글: 클릭 즉시 UI 반영, API 실패 시 롤백
  - [ ] 삭제: 즉시 UI에서 제거, API 실패 시 복원

- [ ] **3-F 검증** 태그/검색/우선순위 통합 테스트
  - [ ] 리마인더에 태그 추가 → 태그 브라우저에 표시 확인
  - [ ] 태그 클릭 → 필터링 확인
  - [ ] 검색 입력 → 실시간 결과 확인
  - [ ] 우선순위 마크 + 플래그 아이콘 표시 확인

---

## Phase 4: 인증 (회원가입/로그인)

### Backend

- [ ] **4-B1** User 엔티티
  - [ ] id, email(unique), password, name, createdAt 필드
  - [ ] UserRepository

- [ ] **4-B2** Security 의존성 추가
  - [ ] build.gradle.kts에 spring-boot-starter-security 추가
  - [ ] build.gradle.kts에 jjwt (io.jsonwebtoken) 추가

- [ ] **4-B3** JWT 유틸리티
  - [ ] 토큰 생성 (subject=userId, 만료시간)
  - [ ] 토큰 검증 및 파싱
  - [ ] 액세스 토큰 1시간, 리프레시 토큰 7일
  - [ ] application.yml에 jwt.secret, jwt.expiration 설정

- [ ] **4-B4** SecurityConfig
  - [ ] SecurityFilterChain 설정
  - [ ] JWT 인증 필터 구현 (OncePerRequestFilter)
  - [ ] `/api/auth/**` 경로는 인증 없이 허용
  - [ ] 나머지 `/api/**`는 인증 필요
  - [ ] CORS 설정 유지

- [ ] **4-B5** AuthController
  - [ ] `POST /api/auth/signup` - 회원가입 (email, password, name)
  - [ ] `POST /api/auth/login` - 로그인 (email, password) → JWT 반환
  - [ ] `POST /api/auth/refresh` - 토큰 갱신

- [ ] **4-B6** 비밀번호 BCrypt
  - [ ] PasswordEncoder 빈 등록
  - [ ] 회원가입 시 해시하여 저장
  - [ ] 로그인 시 해시 비교

- [ ] **4-B7** 엔티티에 userId 추가
  - [ ] Reminder, ReminderList, Tag에 `@ManyToOne` user 추가
  - [ ] 모든 Repository 쿼리에 userId 조건 추가
  - [ ] Service에서 인증된 사용자의 데이터만 접근하도록 수정

- [ ] **4-B8** 회원가입 시 기본 리스트 생성
  - [ ] 회원가입 성공 시 "미리알림" 기본 리스트 자동 생성

- [ ] **4-B 검증** 인증 API 테스트
  - [ ] 회원가입 → 로그인 → JWT 토큰 발급 확인
  - [ ] 토큰 없이 API 접근 → 401 Unauthorized 확인
  - [ ] 토큰 포함 API 접근 → 정상 응답 확인
  - [ ] 사용자 A ↔ B 데이터 격리 확인

### Frontend

- [ ] **4-F1** 로그인 페이지
  - [ ] `app/login/page.tsx`
  - [ ] 이메일 + 비밀번호 입력 폼
  - [ ] Apple 스타일 디자인 (중앙 정렬, 라운드 카드)
  - [ ] 에러 메시지 표시
  - [ ] 회원가입 링크

- [ ] **4-F2** 회원가입 페이지
  - [ ] `app/signup/page.tsx`
  - [ ] 이름 + 이메일 + 비밀번호 입력 폼
  - [ ] 클라이언트 유효성 검사 (이메일 형식, 비밀번호 8자 이상)
  - [ ] 중복 이메일 에러 처리
  - [ ] 성공 시 자동 로그인 → 메인 페이지 이동

- [ ] **4-F3** Auth 상태 관리
  - [ ] `stores/authStore.ts`: Zustand로 user, token, isAuthenticated 관리
  - [ ] localStorage에 accessToken, refreshToken 저장/삭제

- [ ] **4-F4** API client JWT 인터셉터
  - [ ] 모든 API 요청에 `Authorization: Bearer {token}` 헤더 추가
  - [ ] lib/api.ts 수정

- [ ] **4-F5** 토큰 만료 처리
  - [ ] 401 응답 시 refreshToken으로 갱신 시도
  - [ ] 갱신 실패 시 로그인 페이지로 리다이렉트

- [ ] **4-F6** 로그아웃
  - [ ] 사이드바 하단 또는 헤더에 로그아웃 버튼
  - [ ] 클릭 시 토큰 삭제 → 로그인 페이지 이동

- [ ] **4-F7** 미인증 리다이렉트
  - [ ] Next.js middleware로 미인증 사용자를 `/login`으로 리다이렉트
  - [ ] `/login`, `/signup`은 인증 없이 접근 가능

- [ ] **4-F 검증** 인증 통합 테스트
  - [ ] 회원가입 → 자동 로그인 → 메인 화면 이동 확인
  - [ ] 로그아웃 → 로그인 페이지 이동 확인
  - [ ] 미인증 접근 → 로그인 리다이렉트 확인
  - [ ] 서로 다른 계정의 데이터 격리 확인

---

## Phase 5: 반복 일정 + 드래그 앤 드롭 + 정렬

### Backend

- [ ] **5-B1** Recurrence 필드 추가
  - [ ] Recurrence Embeddable 또는 JSON 타입 (type, interval, unit)
  - [ ] RecurrenceType enum (DAILY, WEEKLY, BIWEEKLY, MONTHLY, YEARLY, CUSTOM)
  - [ ] Reminder에 recurrence 필드 추가

- [ ] **5-B2** 반복 완료 로직
  - [ ] toggleComplete 시 recurrence 설정 확인
  - [ ] 설정 있으면 다음 일정 계산하여 새 리마인더 생성
  - [ ] 원본은 완료 처리

- [ ] **5-B3** 리마인더 순서 변경 API
  - [ ] `PATCH /api/reminders/reorder`
  - [ ] body: `[{id: 1, sortOrder: 0}, {id: 3, sortOrder: 1}, ...]`
  - [ ] 벌크 업데이트

- [ ] **5-B4** 정렬 파라미터
  - [ ] `GET /api/reminders?sort=dueDate|priority|title|createdAt|manual`
  - [ ] Repository에 동적 정렬 쿼리 추가

- [ ] **5-B 검증** 반복/순서 API 테스트
  - [ ] 반복 리마인더 완료 → 다음 일정 자동 생성 확인
  - [ ] reorder API → sortOrder 변경 확인
  - [ ] sort 파라미터별 정렬 확인

### Frontend

- [ ] **5-F1** 반복 설정 UI
  - [ ] 상세 편집 패널에 "반복" 필드 추가
  - [ ] 드롭다운: 안 함/매일/매주/격주/매월/매년/사용자 지정
  - [ ] 사용자 지정 시 간격(N) + 단위(일/주/월) 입력

- [ ] **5-F2** 반복 아이콘 표시
  - [ ] ReminderItem에 반복 설정 시 반복 아이콘(🔄) 표시

- [ ] **5-F3** @dnd-kit 드래그 앤 드롭
  - [ ] `npm install @dnd-kit/core @dnd-kit/sortable @dnd-kit/utilities`
  - [ ] ReminderList를 SortableContext로 감싸기
  - [ ] 드래그 핸들 구현
  - [ ] 드롭 시 reorder API 호출

- [ ] **5-F4** 정렬 드롭다운
  - [ ] 리스트 헤더에 정렬 옵션 드롭다운
  - [ ] 옵션: 수동/기한/생성일/우선순위/제목순
  - [ ] 선택 시 API 재요청 또는 클라이언트 정렬

- [ ] **5-F5** 드래그 시각적 피드백
  - [ ] 드래그 중 아이템에 그림자 + 스케일 효과
  - [ ] 드롭 가능 영역 하이라이트

- [ ] **5-F 검증** 반복/DnD 통합 테스트
  - [ ] 반복 리마인더 완료 → 새 리마인더 생성 확인
  - [ ] 드래그 앤 드롭 → 순서 변경 → 새로고침 후 유지 확인
  - [ ] 정렬 옵션 변경 → 올바른 정렬 확인

---

## Phase 6: 애니메이션 + 다크모드 + 반응형 + 폴리싱

### Frontend

- [ ] **6-F1** 완료 체크 애니메이션
  - [ ] Framer Motion: 원형 체크박스 stroke 애니메이션
  - [ ] 체크마크 draw → 텍스트 fade → 아이템 슬라이드 아웃

- [ ] **6-F2** 리스트 전환 트랜지션
  - [ ] 사이드바 리스트 클릭 시 콘텐츠 영역 슬라이드/페이드
  - [ ] AnimatePresence로 exit/enter 관리

- [ ] **6-F3** 상세 패널 애니메이션
  - [ ] 우측에서 슬라이드인
  - [ ] 닫을 때 슬라이드아웃

- [ ] **6-F4** 리스트 아이템 레이아웃 애니메이션
  - [ ] 추가 시 높이 확장 + fade in
  - [ ] 제거 시 높이 축소 + fade out
  - [ ] AnimatePresence + layoutId

- [ ] **6-F5** 다크모드/라이트모드
  - [ ] Tailwind dark 모드 설정 (class 기반)
  - [ ] 시스템 설정 감지 (prefers-color-scheme)
  - [ ] 수동 토글 버튼 (헤더)
  - [ ] 모든 컴포넌트에 dark: 변형 적용

- [ ] **6-F6** 모바일 반응형: 사이드바
  - [ ] 768px 이하에서 사이드바 숨김
  - [ ] 햄버거 메뉴 버튼으로 오버레이 사이드바 토글
  - [ ] 사이드바 외부 클릭 시 닫기

- [ ] **6-F7** 태블릿 반응형: 축소 사이드바
  - [ ] 768px~1024px에서 사이드바 아이콘 모드 (폭 60px)
  - [ ] 호버 시 확장

- [ ] **6-F8** 컨텍스트 메뉴
  - [ ] 리마인더 우클릭 시 커스텀 메뉴
  - [ ] 옵션: 삭제, 플래그 토글, 리스트 이동, 우선순위 변경

- [ ] **6-F9** 빈 상태 UI
  - [ ] 리스트에 리마인더 없을 때 안내 일러스트 + 메시지
  - [ ] 스마트 리스트별 적절한 메시지

- [ ] **6-F10** 로딩 스켈레톤
  - [ ] 사이드바 로딩 스켈레톤
  - [ ] 리마인더 목록 로딩 스켈레톤
  - [ ] 상세 패널 로딩 스켈레톤

- [ ] **6-F11** UI 폴리싱
  - [ ] 간격/패딩 미세 조정 (Apple 기준)
  - [ ] 폰트 크기/굵기 최적화
  - [ ] 호버/포커스/액티브 상태 스타일
  - [ ] 스크롤바 커스텀 스타일
  - [ ] 전체 색상 톤 통일

- [ ] **6-F 검증** 최종 QA
  - [ ] 완료 애니메이션 부드러움 확인 (60fps)
  - [ ] 다크모드 ↔ 라이트모드 전환 확인
  - [ ] 모바일 (375px) 전체 기능 동작 확인
  - [ ] 태블릿 (768px) 레이아웃 확인
  - [ ] 데스크탑 (1440px) 풀 레이아웃 확인
  - [ ] 전체 User Story 시나리오 통과 확인

---

## Progress Summary

| Phase | Backend | Frontend | 상태 |
|-------|---------|----------|------|
| 1 | 8/8 | 10/12 | 🟡 브라우저 검증 필요 |
| 2 | 0/8 | 0/11 | ⬜ 미착수 |
| 3 | 0/5 | 0/10 | ⬜ 미착수 |
| 4 | 0/9 | 0/8 | ⬜ 미착수 |
| 5 | 0/5 | 0/6 | ⬜ 미착수 |
| 6 | - | 0/12 | ⬜ 미착수 |
| **합계** | **0/35** | **0/59** | **0/94** |
