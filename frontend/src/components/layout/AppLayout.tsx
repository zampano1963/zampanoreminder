"use client";

import Sidebar from "./Sidebar";

export default function AppLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex h-full w-full">
      <Sidebar />
      <main className="flex-1 h-full overflow-y-auto" style={{ background: "var(--bg-primary)" }}>
        {children}
      </main>
    </div>
  );
}
