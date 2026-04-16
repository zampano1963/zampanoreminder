# PRD: Zampano Reminder - Apple Reminders Web Clone

## 1. Introduction / Overview

Apple Reminders 앱의 웹 버전을 개발한다. macOS/iOS의 Reminders 앱과 동일한 기능과 Apple 스타일 UI/UX를 웹 브라우저에서 제공하는 것이 목표이다.

- **Backend**: Spring Boot 4 + JPA/H2 REST API (기존 프로젝트 확장)
- **Frontend**: Next.js (latest) - `frontend/` 하위 디렉토리 모노레포 구성
- **인증**: 이메일/비밀번호 기본 인증 (JWT)

---

## 2. Goals

- Apple Reminders의 핵심 기능을 웹에서 동일하게 사용할 수 있다
- Apple의 디자인 언어(SF Pro 스타일, 블러, 라운드 카드, 애니메이션)를 웹에서 재현한다
- 리마인더 생성/조회/수정/삭제/완료를 직관적으로 수행할 수 있다
- 리스트, 태그, 스마트 리스트로 리마인더를 체계적으로 관리할 수 있다
- 반복 일정, 우선순위, 플래그 등 고급 기능을 지원한다
- 이메일/비밀번호 인증으로 개인 데이터를 보호한다

---

## 3. User Stories

### 인증

#### US-001: 회원가입
**Description:** 사용자는 이메일과 비밀번호로 계정을 생성할 수 있다.

**Acceptance Criteria:**
- [ ] 이메일, 비밀번호, 이름 입력 폼이 제공된다
- [ ] 이메일 형식이 유효하지 않으면 에러 메시지를 표시한다
- [ ] 비밀번호는 최소 8자 이상이어야 한다
- [ ] 중복 이메일로 가입 시 에러 메시지를 표시한다
- [ ] 가입 성공 시 자동 로그인되어 메인 화면으로 이동한다
- [ ] 브라우저에서 가입/로그인 화면이 정상 표시되는지 확인

#### US-002: 로그인 / 로그아웃
**Description:** 사용자는 이메일과 비밀번호로 로그인하고, 로그아웃할 수 있다.

**Acceptance Criteria:**
- [ ] 이메일, 비밀번호 입력 후 로그인 버튼으로 인증한다
- [ ] 로그인 성공 시 JWT 토큰을 발급받아 이후 요청에 사용한다
- [ ] 잘못된 자격 증명 시 에러 메시지를 표시한다
- [ ] 로그아웃 시 토큰을 삭제하고 로그인 화면으로 이동한다
- [ ] 토큰 만료 시 자동으로 로그인 화면으로 리다이렉트한다

---

### 리마인더 CRUD

#### US-003: 리마인더 생성
**Description:** 사용자는 새로운 리마인더를 생성할 수 있다.

**Acceptance Criteria:**
- [ ] 제목(필수), 메모, URL, 날짜/시간, 우선순위, 플래그를 입력할 수 있다
- [ ] 리스트(카테고리)를 선택할 수 있다 (미선택 시 기본 리스트)
- [ ] 태그를 추가할 수 있다 (기존 태그 선택 또는 새 태그 생성)
- [ ] 반복 일정을 설정할 수 있다 (매일/매주/매월/매년/사용자 지정)
- [ ] 저장 시 목록에 즉시 반영된다

#### US-004: 리마인더 수정
**Description:** 사용자는 기존 리마인더의 모든 속성을 수정할 수 있다.

**Acceptance Criteria:**
- [ ] 리마인더를 클릭하면 상세 편집 패널이 열린다
- [ ] 제목, 메모, 날짜/시간, 우선순위, 플래그, 태그, 리스트를 변경할 수 있다
- [ ] 변경 사항은 즉시 저장된다 (auto-save 또는 명시적 저장)
- [ ] 수정 후 목록에 즉시 반영된다

#### US-005: 리마인더 완료 / 미완료
**Description:** 사용자는 리마인더를 완료 처리하거나 완료를 취소할 수 있다.

**Acceptance Criteria:**
- [ ] 리마인더 좌측의 원형 체크박스를 클릭하면 완료 처리된다
- [ ] 완료 시 체크 애니메이션이 표시된 후 목록에서 사라진다
- [ ] "완료됨" 스마트 리스트에서 완료된 리마인더를 확인할 수 있다
- [ ] 완료된 리마인더를 다시 미완료로 전환할 수 있다

#### US-006: 리마인더 삭제
**Description:** 사용자는 리마인더를 영구 삭제할 수 있다.

**Acceptance Criteria:**
- [ ] 리마인더를 우클릭 또는 스와이프하여 삭제 옵션을 선택할 수 있다
- [ ] 삭제 전 확인 다이얼로그를 표시한다
- [ ] 삭제 후 목록에서 즉시 제거된다

---

### 리스트 (카테고리) 관리

#### US-007: 리스트 CRUD
**Description:** 사용자는 리마인더를 분류하는 리스트를 관리할 수 있다.

**Acceptance Criteria:**
- [ ] 새 리스트를 생성할 수 있다 (이름, 색상, 아이콘 선택)
- [ ] Apple Reminders처럼 12가지 색상 팔레트를 제공한다
- [ ] 리스트 이름과 색상/아이콘을 수정할 수 있다
- [ ] 리스트를 삭제하면 포함된 리마인더도 삭제된다 (확인 필요)
- [ ] 사이드바에 모든 리스트가 표시되며, 각 리스트의 미완료 카운트를 보여준다

#### US-008: 리스트별 리마인더 보기
**Description:** 사용자는 특정 리스트에 속한 리마인더만 필터링하여 볼 수 있다.

**Acceptance Criteria:**
- [ ] 사이드바에서 리스트를 클릭하면 해당 리스트의 리마인더만 표시된다
- [ ] 리스트 내에서 리마인더를 추가하면 자동으로 해당 리스트에 배정된다

---

### 스마트 리스트

#### US-009: 스마트 리스트 (오늘 / 예정 / 전체 / 플래그 / 완료됨)
**Description:** 사용자는 조건별 자동 필터링 스마트 리스트를 사용할 수 있다.

**Acceptance Criteria:**
- [ ] **오늘**: 오늘 날짜에 해당하는 리마인더를 표시한다
- [ ] **예정**: 날짜가 설정된 미래의 모든 리마인더를 날짜별로 그룹핑하여 표시한다
- [ ] **전체**: 모든 미완료 리마인더를 리스트별로 그룹핑하여 표시한다
- [ ] **플래그**: 플래그가 설정된 리마인더만 표시한다
- [ ] **완료됨**: 완료된 리마인더를 표시한다
- [ ] 각 스마트 리스트의 카운트가 사이드바 상단 카드에 표시된다

---

### 태그

#### US-010: 태그 관리
**Description:** 사용자는 리마인더에 태그를 붙여 분류할 수 있다.

**Acceptance Criteria:**
- [ ] 리마인더 편집 시 `#태그명` 형태로 태그를 추가할 수 있다
- [ ] 기존 태그 목록에서 선택하거나 새 태그를 생성할 수 있다
- [ ] 사이드바 하단에 태그 브라우저가 표시된다
- [ ] 태그를 클릭하면 해당 태그가 달린 리마인더만 필터링된다
- [ ] 여러 태그를 조합하여 필터링할 수 있다 (AND 조건)

---

### 우선순위 & 플래그

#### US-011: 우선순위 설정
**Description:** 사용자는 리마인더에 우선순위를 설정할 수 있다.

**Acceptance Criteria:**
- [ ] 없음 / 낮음 / 보통 / 높음 4단계로 설정할 수 있다
- [ ] 높음 우선순위는 제목 앞에 `!!!` 마크를 표시한다
- [ ] 보통은 `!!`, 낮음은 `!`을 표시한다

#### US-012: 플래그 설정
**Description:** 사용자는 리마인더에 플래그(중요 표시)를 토글할 수 있다.

**Acceptance Criteria:**
- [ ] 리마인더에 플래그 아이콘을 클릭하여 토글할 수 있다
- [ ] 플래그된 리마인더는 주황색 플래그 아이콘이 표시된다
- [ ] "플래그" 스마트 리스트에서 모아볼 수 있다

---

### 반복 일정

#### US-013: 반복 리마인더
**Description:** 사용자는 리마인더를 반복 일정으로 설정할 수 있다.

**Acceptance Criteria:**
- [ ] 반복 옵션: 매일 / 매주 / 격주 / 매월 / 매년 / 사용자 지정
- [ ] 사용자 지정: N일/주/월 간격 설정 가능
- [ ] 반복 리마인더를 완료하면 다음 반복 일정의 리마인더가 자동 생성된다
- [ ] 반복 설정을 해제하면 이후 반복이 중단된다

---

### 검색

#### US-014: 리마인더 검색
**Description:** 사용자는 키워드로 리마인더를 검색할 수 있다.

**Acceptance Criteria:**
- [ ] 상단 검색바에서 제목, 메모 내용으로 검색할 수 있다
- [ ] 입력 중 실시간으로 결과가 필터링된다 (debounce 300ms)
- [ ] 검색 결과는 리스트별로 그룹핑되어 표시된다
- [ ] 검색어와 일치하는 부분이 하이라이트된다

---

### 정렬 & 드래그 앤 드롭

#### US-015: 리마인더 정렬 및 수동 순서 변경
**Description:** 사용자는 리마인더를 정렬하거나 드래그로 순서를 변경할 수 있다.

**Acceptance Criteria:**
- [ ] 수동 순서, 기한, 생성일, 우선순위, 제목순으로 정렬할 수 있다
- [ ] 드래그 앤 드롭으로 리마인더의 수동 순서를 변경할 수 있다
- [ ] 변경된 순서는 서버에 저장된다

---

## 4. Functional Requirements

### Backend (Spring Boot 4 API 확장)

#### 인증
- **FR-001**: User 엔티티 추가 (id, email, password, name, createdAt)
- **FR-002**: 비밀번호는 BCrypt로 해시하여 저장한다
- **FR-003**: 로그인 시 JWT 액세스 토큰(1시간)과 리프레시 토큰(7일)을 발급한다
- **FR-004**: Spring Security를 사용하여 `/api/**` 엔드포인트를 보호한다
- **FR-005**: 회원가입 `POST /api/auth/signup`, 로그인 `POST /api/auth/login`, 토큰 갱신 `POST /api/auth/refresh` 엔드포인트를 제공한다

#### 리마인더
- **FR-006**: Reminder 엔티티 확장 - 기존 필드 + notes, url, priority(enum: NONE/LOW/MEDIUM/HIGH), flagged(boolean), recurrence(JSON 또는 별도 엔티티), sortOrder(int), userId(FK), listId(FK)
- **FR-007**: 서브태스크(Subtask) 엔티티 추가 - id, title, completed, sortOrder, reminderId(FK)
- **FR-008**: 리마인더 CRUD API - `GET/POST /api/reminders`, `GET/PUT/DELETE /api/reminders/{id}`
- **FR-009**: 리마인더 완료 토글 API - `PATCH /api/reminders/{id}/toggle`
- **FR-010**: 리마인더 검색 API - `GET /api/reminders/search?q={keyword}`
- **FR-011**: 리마인더 순서 변경 API - `PATCH /api/reminders/reorder` (body: [{id, sortOrder}])

#### 리스트
- **FR-012**: ReminderList 엔티티 추가 (id, name, color, icon, sortOrder, userId)
- **FR-013**: 리스트 CRUD API - `GET/POST /api/lists`, `GET/PUT/DELETE /api/lists/{id}`
- **FR-014**: 리스트별 리마인더 조회 API - `GET /api/lists/{id}/reminders`
- **FR-015**: 기본 리스트(Reminders)는 유저 생성 시 자동 생성되며 삭제할 수 없다

#### 태그
- **FR-016**: Tag 엔티티 추가 (id, name, userId) + Reminder-Tag 다대다 매핑 테이블
- **FR-017**: 태그 CRUD API - `GET/POST /api/tags`, `DELETE /api/tags/{id}`
- **FR-018**: 태그별 리마인더 필터 API - `GET /api/reminders?tags={tag1,tag2}`

#### 스마트 리스트
- **FR-019**: 스마트 리스트 조회 API - `GET /api/reminders/smart/{type}` (type: today, scheduled, all, flagged, completed)
- **FR-020**: 스마트 리스트 카운트 API - `GET /api/reminders/smart/counts` (각 스마트 리스트의 미완료 카운트)

#### 반복 일정
- **FR-021**: Recurrence 값 객체 (type: DAILY/WEEKLY/BIWEEKLY/MONTHLY/YEARLY/CUSTOM, interval: int, unit: DAY/WEEK/MONTH)
- **FR-022**: 리마인더 완료 시 반복 설정이 있으면 다음 일정의 새 리마인더를 자동 생성한다

### Frontend (Next.js)

#### 레이아웃
- **FR-030**: Apple Reminders와 동일한 2-column 레이아웃 (좌측 사이드바 + 우측 콘텐츠)
- **FR-031**: 사이드바 상단에 스마트 리스트 카드 그리드 (오늘/예정/전체/플래그/완료됨)
- **FR-032**: 사이드바 중간에 "나의 목록" (사용자 리스트 목록)
- **FR-033**: 사이드바 하단에 태그 브라우저
- **FR-034**: 우측 영역에 선택된 리스트의 리마인더 목록 + 상세 편집 패널

#### UI/UX
- **FR-035**: Apple SF Pro 스타일 폰트 (시스템 폰트 스택 사용)
- **FR-036**: Apple 디자인 시스템 색상 (System Blue, System Red 등 12색 팔레트)
- **FR-037**: 리마인더 완료 시 원형 체크 애니메이션 (체크마크 스트로크 애니메이션)
- **FR-038**: 리스트/리마인더 전환 시 부드러운 트랜지션 애니메이션
- **FR-039**: 반응형 레이아웃 - 모바일에서 사이드바 토글 가능
- **FR-040**: 다크모드 / 라이트모드 지원 (시스템 설정 따르기 + 수동 토글)

#### 상태 관리 & API 통신
- **FR-041**: API 통신은 fetch 또는 axios로 처리, JWT 토큰을 Authorization 헤더에 포함
- **FR-042**: 클라이언트 상태 관리: Zustand 또는 React Context
- **FR-043**: 서버 상태 관리: TanStack Query (React Query)로 캐싱 및 동기화
- **FR-044**: Optimistic update 적용 (완료 토글, 삭제 시 즉시 UI 반영 후 서버 동기화)

---

## 5. Non-Goals (Out of Scope)

- **위치 기반 알림**: GPS/위치 트리거는 구현하지 않는다
- **iCloud 동기화**: Apple 생태계 연동은 구현하지 않는다
- **공유/협업**: 리스트를 다른 사용자와 공유하는 기능은 구현하지 않는다
- **첨부파일/이미지**: 리마인더에 파일을 첨부하는 기능은 구현하지 않는다
- **Siri/음성 입력**: 음성 기반 리마인더 생성은 구현하지 않는다
- **푸시 알림**: 브라우저 알림은 이번 범위에 포함하지 않는다 (향후 확장 가능)
- **오프라인 모드**: Service Worker 기반 오프라인 지원은 구현하지 않는다
- **i18n**: 한국어 단일 언어로 개발한다

---

## 6. Design Considerations

### 레이아웃 참조 (Apple Reminders macOS)

```
+------------------------------------------+
| [Search]                        [+] [👤] |
+----------+-------------------------------+
| Smart    |                               |
| Lists    |   Reminder List Area          |
| [Today]  |                               |
| [Sched]  |   ○ Reminder title      🏳️   |
| [All]    |     Notes / Due date          |
| [Flag]   |   ○ Reminder title      !!!   |
| [Done]   |     #tag1 #tag2               |
|----------|                               |
| My Lists |   ○ Reminder title            |
| 🔴 Work  |     May 3, 2026               |
| 🔵 Home  |                               |
| 🟢 Shop  |-------------------------------+
|----------|   Detail Panel (right side)   |
| Tags     |   Title: [          ]         |
| #urgent  |   Notes: [          ]         |
| #work    |   Date:  [May 3  ] [10:00]    |
| #home    |   List:  [Work    ▼]          |
+----------+-------------------------------+
```

### 색상 팔레트 (Apple 12색)
Red, Orange, Yellow, Green, Mint, Teal, Cyan, Blue, Indigo, Purple, Pink, Brown

### 핵심 인터랙션
- 리마인더 완료 체크: 원형 버튼 클릭 시 stroke 애니메이션 + fade out
- 리스트 전환: 콘텐츠 영역 슬라이드 트랜지션
- 리마인더 추가: 리스트 하단 "+ 새로운 미리알림" 인라인 입력
- 상세 편집: 리마인더 클릭 시 우측 패널 슬라이드인

---

## 7. Technical Considerations

### Backend

| 항목 | 기술 |
|------|------|
| Framework | Spring Boot 4.0.5 / Spring Framework 7.0.6 |
| ORM | Spring Data JPA + Hibernate 7.2 |
| DB | H2 (dev), 추후 PostgreSQL 전환 가능 |
| Auth | Spring Security + JWT (jjwt 라이브러리) |
| Java | 25 |
| Build | Gradle Kotlin DSL |

### Frontend

| 항목 | 기술 |
|------|------|
| Framework | Next.js (latest, App Router) |
| Language | TypeScript |
| Styling | Tailwind CSS |
| State | Zustand (client) + TanStack Query (server) |
| DnD | @dnd-kit/core |
| Animation | Framer Motion |
| Icons | Lucide React |
| Forms | React Hook Form |

### API 설계 원칙
- RESTful API, JSON 요청/응답
- 모든 엔드포인트에 JWT 인증 필요 (auth 제외)
- 에러 응답 형식: `{ "error": "message", "code": "ERROR_CODE" }`
- 페이지네이션: 스마트 리스트 및 검색에 커서 기반 페이지네이션 적용

### 프로젝트 구조
```
zampanoreminder/
├── src/                          # Spring Boot Backend
│   ├── main/java/zampano/ai/zampanoreminder/
│   └── main/resources/
├── frontend/                     # Next.js Frontend
│   ├── src/
│   │   ├── app/                  # App Router pages
│   │   ├── components/           # React components
│   │   ├── lib/                  # API client, utils
│   │   ├── stores/               # Zustand stores
│   │   └── types/                # TypeScript types
│   ├── public/
│   ├── package.json
│   └── tailwind.config.ts
├── build.gradle.kts
└── prd.md
```

### CORS 설정
- Backend에서 `http://localhost:3000` (Next.js dev server) 허용 필요
- Production 시 동일 도메인 또는 프록시 설정

---

## 8. Success Metrics

- [ ] 회원가입 → 로그인 → 리마인더 생성 → 완료 흐름이 5번의 클릭 이내에 완료된다
- [ ] 리마인더 CRUD 전체 기능이 정상 동작한다
- [ ] 5개 스마트 리스트(오늘/예정/전체/플래그/완료됨)가 올바르게 필터링된다
- [ ] 리스트/태그 기반 분류가 정상 동작한다
- [ ] 반복 리마인더 완료 시 다음 일정이 자동 생성된다
- [ ] Apple Reminders와 시각적으로 유사한 UI가 브라우저에서 표시된다
- [ ] 완료 애니메이션, 트랜지션이 부드럽게 동작한다 (60fps)
- [ ] 다크모드/라이트모드 전환이 정상 동작한다
- [ ] 모바일 반응형 레이아웃이 정상 동작한다
- [ ] API 응답 시간이 200ms 이내이다 (H2 기준)

---

## 9. Open Questions

1. **H2 → PostgreSQL 전환 시점**: 개발 완료 후 프로덕션 DB로 전환할 것인지, 처음부터 PostgreSQL을 사용할 것인지?
2. **서브태스크**: Apple Reminders의 서브태스크(하위 리마인더) 기능을 포함할 것인지?
3. **리스트 그룹**: 리스트를 그룹으로 묶는 기능(Apple의 Group Lists)을 포함할 것인지?
4. **단축키**: 키보드 단축키 지원이 필요한가? (예: Cmd+N 새 리마인더, Enter 완료)
5. **배포 환경**: 최종 배포 대상 (Vercel + 별도 API 서버, Docker Compose, 단일 서버 등)?
