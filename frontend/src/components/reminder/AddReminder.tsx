"use client";

import { useState } from "react";

interface Props {
  onAdd: (title: string) => void;
}

export default function AddReminder({ onAdd }: Props) {
  const [editing, setEditing] = useState(false);
  const [title, setTitle] = useState("");

  const handleSubmit = () => {
    const trimmed = title.trim();
    if (trimmed) {
      onAdd(trimmed);
      setTitle("");
      setEditing(false);
    }
  };

  if (!editing) {
    return (
      <button
        onClick={() => setEditing(true)}
        className="flex items-center gap-2 px-4 py-2 w-full text-sm transition-colors hover:bg-[var(--bg-secondary)] rounded-lg"
        style={{ color: "var(--apple-blue)" }}
      >
        <span className="text-lg leading-none">+</span>
        <span>New Reminder</span>
      </button>
    );
  }

  return (
    <div className="flex items-center gap-3 px-4 py-2">
      <div
        className="w-5 h-5 rounded-full border-2 shrink-0"
        style={{ borderColor: "var(--text-secondary)" }}
      />
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter") handleSubmit();
          if (e.key === "Escape") {
            setTitle("");
            setEditing(false);
          }
        }}
        onBlur={() => {
          if (!title.trim()) setEditing(false);
        }}
        autoFocus
        placeholder="New Reminder"
        className="flex-1 text-sm bg-transparent outline-none"
        style={{ color: "var(--text-primary)" }}
      />
    </div>
  );
}
