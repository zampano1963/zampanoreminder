# Zampano Reminder - Development Plan

> spec.md 기반으로 단순한 것부터 점진적으로 기능을 추가하는 Phase별 개발 계획

---

## Tech Stack Summary

### Backend (기존 프로젝트 확장)

| 항목 | 기술 | 비고 |
|------|------|------|
| Framework | Spring Boot 4.0.5 | Spring Framework 7.0.6 |
| Java | 25 | |
| ORM | Spring Data JPA | Hibernate 7.2.7 |
| DB | H2 (in-memory) | 개발용, 추후 PostgreSQL 전환 가능 |
| Auth | Spring Security + JWT | jjwt (io.jsonwebtoken) |
| Build | Gradle Kotlin DSL | 9.4.1 |
| API 스타일 | RESTful JSON | Jackson 3.x (Boot 4 기본) |
| 서버 포트 | 9090 | (8080은 httpd가 사용 중) |

### Frontend (신규)

| 항목 | 기술 | 비고 |
|------|------|------|
| Framework | Next.js (latest) | App Router, TypeScript |
| Styling | Tailwind CSS | Apple 디자인 시스템 재현 |
| Server State | TanStack Query (React Query) | 캐싱, Optimistic Update |
| Client State | Zustand | 사이드바 상태, UI 상태 |
| HTTP Client | fetch (native) | JWT 인터셉터 래핑 |
| Animation | Framer Motion | 완료 체크, 트랜지션 |
| DnD | @dnd-kit/core | 리마인더 순서 변경 |
| Icons | Lucide React | |
| Forms | React Hook Form | 리마인더 편집 |
| 개발 서버 포트 | 3000 | |

### 프로젝트 구조

```
zampanoreminder/
├── src/                              # Spring Boot Backend
│   ├── main/java/zampano/ai/zampanoreminder/
│   │   ├── config/                   # Security, CORS, JWT 설정
│   │   ├── controller/               # REST Controllers
│   │   ├── dto/                      # Request/Response DTOs
│   │   ├── entity/                   # JPA Entities
│   │   ├── repository/               # Spring Data Repositories
│   │   └── service/                  # Business Logic
│   └── main/resources/
│       └── application.yml
├── frontend/                         # Next.js Frontend
│   ├── src/
│   │   ├── app/                      # App Router (pages, layouts)
│   │   ├── components/               # React Components
│   │   │   ├── layout/               # Sidebar, Header, Layout
│   │   │   ├── reminder/             # Reminder 관련 컴포넌트
│   │   │   ├── list/                 # List 관련 컴포넌트
│   │   │   └── ui/                   # 공통 UI (Button, Input, Modal...)
│   │   ├── lib/                      # API client, auth, utils
│   │   ├── stores/                   # Zustand stores
│   │   ├── hooks/                    # Custom hooks
│   │   └── types/                    # TypeScript type definitions
│   ├── public/
│   ├── package.json
│   ├── tailwind.config.ts
│   ├── tsconfig.json
│   └── next.config.ts
├── build.gradle.kts
├── spec.md
└── plan.md
```

---

## Phase 1: 기본 리마인더 CRUD + Next.js 셋업

> 가장 단순한 형태. 인증 없이, 리마인더 생성/조회/수정/삭제/완료만 동작하는 풀스택.

### 목표
- Next.js 프로젝트 초기화 및 Backend 연동 확인
- 리마인더 기본 CRUD가 웹 UI에서 동작
- Apple 스타일 기본 레이아웃 골격 완성

### Backend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 1-B1 | Reminder 엔티티 확장: notes, url, priority(enum), flagged, sortOrder 필드 추가 | FR-006 |
| 1-B2 | ReminderDTO (Request/Response) 생성 | FR-008 |
| 1-B3 | ReminderController 확장: PUT (수정), PATCH toggle (완료 토글) 추가 | FR-008, FR-009 |
| 1-B4 | ReminderService 확장: update, toggleComplete 메서드 추가 | FR-008, FR-009 |
| 1-B5 | CORS 설정: `http://localhost:3000` 허용 | Technical |
| 1-B6 | 글로벌 에러 핸들러 추가 (RestControllerAdvice) | Technical |
| 1-B7 | DataInitializer 샘플 데이터 확장 (다양한 리마인더) | - |

### Frontend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 1-F1 | Next.js 프로젝트 초기화 (`frontend/`): TypeScript, Tailwind CSS, App Router | Technical |
| 1-F2 | API client 모듈 생성 (`lib/api.ts`): fetch 래퍼, base URL 설정 | FR-041 |
| 1-F3 | TypeScript 타입 정의 (`types/reminder.ts`): Reminder, Priority enum | Technical |
| 1-F4 | 기본 레이아웃 구현: 2-column (사이드바 placeholder + 메인 영역) | FR-030 |
| 1-F5 | 리마인더 목록 컴포넌트: 전체 리마인더 조회 및 표시 | US-009 (전체) |
| 1-F6 | 리마인더 생성 컴포넌트: 하단 인라인 입력 ("+ 새로운 미리알림") | US-003 |
| 1-F7 | 리마인더 완료 토글: 원형 체크박스 클릭으로 완료/미완료 전환 | US-005 |
| 1-F8 | 리마인더 삭제: 컨텍스트 메뉴 또는 삭제 버튼 | US-006 |
| 1-F9 | 리마인더 상세 편집 패널: 클릭 시 우측 패널에서 제목/메모/날짜/우선순위 수정 | US-004 |
| 1-F10 | Apple 스타일 기초: 시스템 폰트, 12색 팔레트 CSS 변수, 라운드 카드 | FR-035, FR-036 |

### 완료 기준
- [ ] `./gradlew bootRun`으로 Backend 실행, `npm run dev`로 Frontend 실행
- [ ] 웹 브라우저에서 리마인더 생성/조회/수정/삭제/완료 동작 확인
- [ ] Apple 스타일 2-column 레이아웃이 렌더링됨

---

## Phase 2: 리스트(카테고리) + 스마트 리스트

> 리마인더를 리스트로 분류하고, 스마트 리스트로 자동 필터링.

### 목표
- 사용자 정의 리스트(카테고리) 생성/관리
- 스마트 리스트 5종 (오늘/예정/전체/플래그/완료됨)
- 사이드바 완성

### Backend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 2-B1 | ReminderList 엔티티 생성: id, name, color, icon, sortOrder | FR-012 |
| 2-B2 | Reminder 엔티티에 listId(FK) 추가, 연관관계 설정 | FR-006 |
| 2-B3 | ReminderListController: CRUD API | FR-013 |
| 2-B4 | 리스트별 리마인더 조회 API: `GET /api/lists/{id}/reminders` | FR-014 |
| 2-B5 | 스마트 리스트 API: `GET /api/reminders/smart/{type}` (today/scheduled/all/flagged/completed) | FR-019 |
| 2-B6 | 스마트 리스트 카운트 API: `GET /api/reminders/smart/counts` | FR-020 |
| 2-B7 | 기본 리스트 "미리알림" 자동 생성 (앱 시작 시) | FR-015 |

### Frontend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 2-F1 | 사이드바 상단: 스마트 리스트 카드 그리드 (아이콘 + 카운트) | FR-031, US-009 |
| 2-F2 | 사이드바 중간: "나의 목록" 리스트 (색상 도트 + 이름 + 카운트) | FR-032, US-007 |
| 2-F3 | 리스트 생성 다이얼로그: 이름, 12색 팔레트, 아이콘 선택 | US-007 |
| 2-F4 | 리스트 수정/삭제 기능 | US-007 |
| 2-F5 | 사이드바 리스트 클릭 시 해당 리스트의 리마인더만 표시 | US-008 |
| 2-F6 | 스마트 리스트 클릭 시 필터링된 결과 표시 (오늘/예정/전체/플래그/완료됨) | US-009 |
| 2-F7 | "예정" 스마트 리스트: 날짜별 그룹핑 | US-009 |
| 2-F8 | "전체" 스마트 리스트: 리스트별 그룹핑 | US-009 |
| 2-F9 | 리마인더 생성 시 현재 선택된 리스트에 자동 배정 | US-008 |
| 2-F10 | TanStack Query 도입: 리스트/리마인더 캐싱 및 동기화 | FR-043 |

### 완료 기준
- [ ] 리스트를 생성하고 색상/아이콘을 설정할 수 있다
- [ ] 사이드바에서 리스트를 선택하면 해당 리마인더만 표시된다
- [ ] 5개 스마트 리스트가 올바르게 필터링된다
- [ ] 각 스마트 리스트와 사용자 리스트의 카운트가 표시된다

---

## Phase 3: 태그 + 검색 + 우선순위/플래그 UI

> 분류와 검색 기능으로 리마인더 관리 고도화.

### 목표
- 태그 시스템으로 리마인더 교차 분류
- 실시간 검색
- 우선순위/플래그 시각적 표현 완성

### Backend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 3-B1 | Tag 엔티티 생성 + Reminder-Tag 다대다 매핑 (JoinTable) | FR-016 |
| 3-B2 | Tag CRUD API: `GET/POST /api/tags`, `DELETE /api/tags/{id}` | FR-017 |
| 3-B3 | 태그 필터 API: `GET /api/reminders?tags=tag1,tag2` | FR-018 |
| 3-B4 | 검색 API: `GET /api/reminders/search?q={keyword}` (title, notes LIKE 검색) | FR-010 |

### Frontend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 3-F1 | 우선순위 표시: 리마인더 목록에서 `!` / `!!` / `!!!` 마크 표시 | US-011 |
| 3-F2 | 우선순위 선택 UI: 상세 편집 패널에서 4단계 선택 | US-011 |
| 3-F3 | 플래그 토글: 리마인더 우측 플래그 아이콘 클릭 | US-012 |
| 3-F4 | 태그 입력: 상세 편집 패널에서 `#` 입력으로 태그 추가 (자동완성) | US-010 |
| 3-F5 | 태그 브라우저: 사이드바 하단에 태그 목록 표시 | FR-033, US-010 |
| 3-F6 | 태그 필터링: 태그 클릭 시 해당 태그의 리마인더만 표시 | US-010 |
| 3-F7 | 검색바: 상단에 검색 입력, debounce 300ms 실시간 필터링 | US-014 |
| 3-F8 | 검색 결과: 리스트별 그룹핑 + 매칭 텍스트 하이라이트 | US-014 |
| 3-F9 | Optimistic Update 적용: 완료 토글, 플래그 토글, 삭제 시 즉시 UI 반영 | FR-044 |

### 완료 기준
- [ ] 리마인더에 태그를 추가/제거할 수 있다
- [ ] 사이드바 태그 브라우저에서 태그별 필터링이 동작한다
- [ ] 검색바에 키워드 입력 시 실시간으로 결과가 표시된다
- [ ] 우선순위 마크와 플래그 아이콘이 올바르게 표시된다

---

## Phase 4: 인증 (회원가입/로그인)

> JWT 기반 인증 추가. 기존 기능을 사용자별 데이터로 격리.

### 목표
- 이메일/비밀번호 회원가입 + 로그인
- JWT 토큰 기반 API 보호
- 사용자별 데이터 격리

### Backend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 4-B1 | User 엔티티 생성: id, email, password(hashed), name, createdAt | FR-001 |
| 4-B2 | build.gradle.kts에 spring-boot-starter-security, jjwt 의존성 추가 | Technical |
| 4-B3 | JWT 유틸리티: 토큰 생성/검증/파싱 (액세스 1시간, 리프레시 7일) | FR-003 |
| 4-B4 | SecurityConfig: JWT 필터, 엔드포인트 보호 (`/api/auth/**` 제외) | FR-004 |
| 4-B5 | AuthController: signup, login, refresh 엔드포인트 | FR-005 |
| 4-B6 | 비밀번호 BCrypt 해시 | FR-002 |
| 4-B7 | 모든 엔티티에 userId(FK) 추가, 쿼리에 userId 조건 적용 | - |
| 4-B8 | 회원가입 시 기본 리스트 자동 생성 | FR-015 |

### Frontend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 4-F1 | 로그인 페이지: 이메일/비밀번호 입력 폼, Apple 스타일 | US-002 |
| 4-F2 | 회원가입 페이지: 이메일/비밀번호/이름 입력 폼, 유효성 검사 | US-001 |
| 4-F3 | Auth 상태 관리: Zustand store + localStorage 토큰 저장 | FR-041 |
| 4-F4 | API client에 JWT Authorization 헤더 인터셉터 추가 | FR-041 |
| 4-F5 | 토큰 만료 시 자동 리프레시 또는 로그인 리다이렉트 | US-002 |
| 4-F6 | 로그아웃 버튼 (사이드바 하단 또는 헤더) | US-002 |
| 4-F7 | 미인증 시 로그인 페이지로 리다이렉트 (middleware) | US-002 |

### 완료 기준
- [ ] 회원가입 후 자동 로그인되어 메인 화면으로 이동한다
- [ ] 로그인/로그아웃이 정상 동작한다
- [ ] 비로그인 상태에서 메인 페이지 접근 시 로그인 페이지로 이동한다
- [ ] 사용자 A의 데이터가 사용자 B에게 보이지 않는다

---

## Phase 5: 반복 일정 + 드래그 앤 드롭 + 정렬

> 고급 기능 추가로 Apple Reminders 수준의 기능 완성.

### 목표
- 반복 리마인더 (매일/매주/매월/매년/사용자 지정)
- 드래그 앤 드롭 순서 변경
- 다양한 정렬 옵션

### Backend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 5-B1 | Reminder 엔티티에 recurrence 필드 추가 (JSON 또는 Embeddable) | FR-021 |
| 5-B2 | 반복 로직: 완료 시 다음 일정 리마인더 자동 생성 | FR-022 |
| 5-B3 | 리마인더 순서 변경 API: `PATCH /api/reminders/reorder` | FR-011 |
| 5-B4 | 정렬 파라미터 지원: `GET /api/reminders?sort=dueDate,priority,title,manual` | US-015 |

### Frontend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 5-F1 | 반복 설정 UI: 상세 편집 패널에서 반복 타입/간격 선택 | US-013 |
| 5-F2 | 반복 아이콘 표시: 리마인더 목록에서 반복 설정 여부 표시 | US-013 |
| 5-F3 | @dnd-kit 통합: 리마인더 드래그 앤 드롭 순서 변경 | US-015 |
| 5-F4 | 정렬 드롭다운: 수동/기한/생성일/우선순위/제목순 | US-015 |
| 5-F5 | 드래그 시 시각적 피드백 (그림자, 스케일 애니메이션) | US-015 |

### 완료 기준
- [ ] 반복 리마인더를 완료하면 다음 일정이 자동 생성된다
- [ ] 드래그 앤 드롭으로 리마인더 순서를 변경할 수 있다
- [ ] 정렬 옵션 변경 시 목록이 즉시 재정렬된다

---

## Phase 6: 애니메이션 + 다크모드 + 반응형 + 폴리싱

> Apple 수준의 UI/UX 완성도를 달성.

### 목표
- Apple Reminders 수준의 애니메이션과 인터랙션
- 다크모드/라이트모드
- 모바일 반응형
- 전체적인 UI 폴리싱

### Frontend 작업

| # | 작업 | 관련 Spec |
|---|------|-----------|
| 6-F1 | Framer Motion: 완료 체크 애니메이션 (원형 stroke → 체크마크 → fade out) | FR-037 |
| 6-F2 | 리스트 전환 트랜지션: 콘텐츠 영역 슬라이드/페이드 | FR-038 |
| 6-F3 | 상세 패널 슬라이드인 애니메이션 | FR-038 |
| 6-F4 | 리스트 아이템 추가/제거 시 레이아웃 애니메이션 (AnimatePresence) | FR-038 |
| 6-F5 | 다크모드/라이트모드: CSS 변수 기반, 시스템 설정 감지 + 수동 토글 | FR-040 |
| 6-F6 | 반응형: 모바일에서 사이드바 오버레이 토글 (햄버거 메뉴) | FR-039 |
| 6-F7 | 반응형: 태블릿에서 사이드바 축소 모드 | FR-039 |
| 6-F8 | 컨텍스트 메뉴: 리마인더 우클릭 시 삭제/플래그/이동 메뉴 | US-006 |
| 6-F9 | 빈 상태 UI: 리스트가 비어있을 때 안내 메시지 | - |
| 6-F10 | 로딩 스켈레톤 UI | - |
| 6-F11 | 전체 UI 폴리싱: 간격, 폰트 크기, 색상 미세 조정 | FR-035, FR-036 |

### 완료 기준
- [ ] 완료 애니메이션이 Apple Reminders와 유사하게 동작한다
- [ ] 다크모드/라이트모드 전환이 매끄럽다
- [ ] 모바일 (375px~)에서 사이드바 토글 + 전체 기능 사용 가능
- [ ] 모든 인터랙션에 적절한 피드백 (호버, 클릭, 로딩)이 있다

---

## Phase Summary

| Phase | 핵심 내용 | Backend | Frontend |
|-------|-----------|---------|----------|
| **1** | 기본 CRUD + Next.js 셋업 | 엔티티 확장, CORS | 프로젝트 초기화, 기본 레이아웃, CRUD UI |
| **2** | 리스트 + 스마트 리스트 | List 엔티티, 스마트 리스트 API | 사이드바, 리스트 관리, 필터링 |
| **3** | 태그 + 검색 + 우선순위/플래그 | Tag 엔티티, 검색 API | 태그 UI, 검색바, 우선순위/플래그 표시 |
| **4** | 인증 | User + Security + JWT | 로그인/회원가입, 토큰 관리 |
| **5** | 반복 일정 + DnD + 정렬 | 반복 로직, 순서 API | 반복 UI, 드래그 앤 드롭, 정렬 |
| **6** | 애니메이션 + 다크모드 + 반응형 | - | Framer Motion, 다크모드, 모바일 |

---

## 개발 순서 근거

1. **Phase 1이 먼저인 이유**: 프론트엔드 프로젝트 셋업과 Backend 연동이 모든 후속 작업의 기반. 가장 단순한 CRUD부터 확인해야 안정적으로 확장 가능.

2. **Phase 2가 Phase 3보다 먼저인 이유**: 리스트는 리마인더의 기본 분류 체계이며, 사이드바 구조를 결정함. 태그/검색보다 앱 뼈대에 가까움.

3. **Phase 4 (인증)를 중간에 배치한 이유**: 초기에는 인증 없이 빠르게 기능을 개발하고, 핵심 기능이 완성된 후 인증을 추가하면 userId 기반 데이터 격리만 추가하면 됨. 너무 일찍 넣으면 개발 중 매번 로그인이 번거로움.

4. **Phase 5-6이 마지막인 이유**: 반복 일정과 DnD는 핵심 CRUD 위에 얹는 고급 기능. 애니메이션/다크모드는 기능 완성 후 폴리싱 단계.
