"use client";

import { Reminder, Priority } from "@/types/reminder";

function priorityMark(priority: Priority): string {
  switch (priority) {
    case Priority.HIGH: return "!!!";
    case Priority.MEDIUM: return "!!";
    case Priority.LOW: return "!";
    default: return "";
  }
}

function priorityColor(priority: Priority): string {
  switch (priority) {
    case Priority.HIGH: return "var(--apple-red)";
    case Priority.MEDIUM: return "var(--apple-orange)";
    case Priority.LOW: return "var(--apple-blue)";
    default: return "";
  }
}

function formatDate(dateStr: string | null): string {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  return date.toLocaleDateString(undefined, {
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

interface Props {
  reminder: Reminder;
  selected: boolean;
  onSelect: (r: Reminder) => void;
  onToggle: (id: number) => void;
  onDelete: (id: number) => void;
}

export default function ReminderItem({ reminder, selected, onSelect, onToggle, onDelete }: Props) {
  const mark = priorityMark(reminder.priority);

  return (
    <div
      role="button"
      tabIndex={0}
      onKeyDown={(e) => { if (e.key === "Enter" || e.key === " ") onSelect(reminder); }}
      className={`group flex items-start gap-3 px-4 py-2.5 cursor-pointer rounded-lg transition-colors ${
        selected ? "bg-[var(--apple-blue)]/10" : "hover:bg-[var(--bg-secondary)]"
      }`}
      onClick={() => onSelect(reminder)}
    >
      <button
        onClick={(e) => {
          e.stopPropagation();
          onToggle(reminder.id);
        }}
        aria-label={reminder.completed ? "Mark as incomplete" : "Mark as complete"}
        className="mt-0.5 w-5 h-5 rounded-full border-2 flex items-center justify-center shrink-0 transition-colors"
        style={{
          borderColor: reminder.completed ? "var(--apple-blue)" : "var(--text-secondary)",
          background: reminder.completed ? "var(--apple-blue)" : "transparent",
        }}
      >
        {reminder.completed && (
          <svg width="10" height="8" viewBox="0 0 10 8" fill="none">
            <path d="M1 4L3.5 6.5L9 1" stroke="white" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        )}
      </button>

      <div className="flex-1 min-w-0">
        <div className="flex items-center gap-1.5">
          {mark && (
            <span className="text-xs font-bold" style={{ color: priorityColor(reminder.priority) }}>
              {mark}
            </span>
          )}
          <span
            className={`text-sm ${reminder.completed ? "line-through" : ""}`}
            style={{ color: reminder.completed ? "var(--text-secondary)" : "var(--text-primary)" }}
          >
            {reminder.title}
          </span>
        </div>
        {(reminder.notes || reminder.remindAt) && (
          <div className="text-xs mt-0.5" style={{ color: "var(--text-secondary)" }}>
            {reminder.notes && <span>{reminder.notes}</span>}
            {reminder.notes && reminder.remindAt && <span> · </span>}
            {reminder.remindAt && <span>{formatDate(reminder.remindAt)}</span>}
          </div>
        )}
      </div>

      {reminder.flagged && (
        <span className="shrink-0" style={{ color: "var(--apple-orange)" }}>⚑</span>
      )}

      <button
        onClick={(e) => {
          e.stopPropagation();
          if (confirm("Delete this reminder?")) onDelete(reminder.id);
        }}
        aria-label="Delete reminder"
        className="text-xs px-1.5 py-0.5 rounded opacity-0 group-hover:opacity-100 transition-opacity hover:bg-red-100"
        style={{ color: "var(--apple-red)" }}
      >
        ✕
      </button>
    </div>
  );
}
