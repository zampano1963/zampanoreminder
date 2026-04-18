# CLAUDE.md - Zampano Reminder

## Project Overview

Apple Reminders 웹 클론. Spring Boot 4 백엔드 + Next.js 프론트엔드 모노레포.

- **Backend**: Spring Boot 4.0.5 / Java 25 / JPA + H2 / Gradle Kotlin DSL
- **Frontend**: Next.js (App Router) / TypeScript / Tailwind CSS (`frontend/`)
- **서버 포트**: Backend 9090, Frontend 3000

## Coding Conventions

### General

- 기능 추가/수정 시 반드시 테스트를 함께 작성한다
- 도메인 엔티티 테스트는 순수 단위 테스트 (JPA/Spring 컨텍스트 없이)
- 서비스/컨트롤러 테스트는 적절한 수준의 통합 테스트 적용
- 한국어 주석/DisplayName 사용, 코드는 영어

### Java / Spring Boot 4

- 패키지 구조: `domain/`, `repository/`, `service/`, `controller/`, `config/`, `dto/`
- 엔티티는 `domain` 패키지에 위치 (`entity` 아님)
- Lombok 사용: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
- 엔티티에 `@Data` 사용 금지 (JPA equals/hashCode 문제)
- `@Builder.Default`로 기본값 설정 (sortOrder, deletable, createdAt 등)
- `createdAt`/`updatedAt`은 `@PrePersist` 대신 `@Builder.Default`로 생성 시 설정, `update()` 메서드에서 갱신
- 엔티티 수정은 setter 직접 호출 대신 의미 있는 `update()` 메서드 사용
- Boot 4 스타터: `spring-boot-starter-webmvc` (not `web`), `spring-boot-h2console` (별도 모듈)
- Boot 4 테스트: `@DataJpaTest` → `org.springframework.boot.data.jpa.test.autoconfigure`, `TestEntityManager` → `org.springframework.boot.jpa.test.autoconfigure`
- `@MockBean` 대신 `@MockitoBean` 사용
- `@SpringBootTest`에 MockMVC 자동설정 안 됨 → `@AutoConfigureMockMvc` 필요
- Jackson 3.x 기본 (`tools.jackson` 패키지)
- `RestTemplate` 사용 금지 → `RestClient` 사용

### Frontend (Next.js)

- App Router 사용
- TypeScript strict mode
- Tailwind CSS로 스타일링
- 컴포넌트: `components/layout/`, `components/reminder/`, `components/list/`, `components/ui/`
- API client: `lib/api.ts`
- 타입: `types/` 디렉토리
- 상태: Zustand (client), TanStack Query (server)

### Git

- 커밋 메시지: 영어, 1~2문장, "why" 중심
- `Co-Authored-By: Claude Opus 4.6 (1M context) <noreply@anthropic.com>` 포함

## Build & Run

```bash
# Backend
./gradlew build
./gradlew bootRun          # http://localhost:9090

# Frontend (향후)
cd frontend && npm run dev  # http://localhost:3000
```

## Key Files

- `spec.md` - 전체 요구사항 (PRD)
- `plan.md` - 6-Phase 개발 계획
- `tasks.md` - 세부 작업 체크리스트
