"use client";

export default function Sidebar() {
  return (
    <aside
      className="w-60 shrink-0 h-full overflow-y-auto border-r"
      style={{
        background: "var(--bg-sidebar)",
        borderColor: "var(--border-color)",
      }}
    >
      <div className="p-4">
        <h2
          className="text-xs font-semibold uppercase tracking-wider mb-3"
          style={{ color: "var(--text-secondary)" }}
        >
          My Lists
        </h2>
        <p
          className="text-sm"
          style={{ color: "var(--text-secondary)" }}
        >
          Phase 2 placeholder
        </p>
      </div>
    </aside>
  );
}
