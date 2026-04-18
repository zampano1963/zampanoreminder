"use client";

import { useState, useEffect } from "react";
import { Reminder, ReminderRequest, Priority } from "@/types/reminder";

interface Props {
  reminder: Reminder;
  onUpdate: (id: number, data: ReminderRequest) => void;
  onClose: () => void;
}

export default function ReminderDetail({ reminder, onUpdate, onClose }: Props) {
  const [title, setTitle] = useState(reminder.title);
  const [notes, setNotes] = useState(reminder.notes || "");
  const [url, setUrl] = useState(reminder.url || "");
  const [remindAt, setRemindAt] = useState(reminder.remindAt?.slice(0, 16) || "");
  const [priority, setPriority] = useState<Priority>(reminder.priority);
  const [flagged, setFlagged] = useState(reminder.flagged);

  useEffect(() => {
    setTitle(reminder.title);
    setNotes(reminder.notes || "");
    setUrl(reminder.url || "");
    setRemindAt(reminder.remindAt?.slice(0, 16) || "");
    setPriority(reminder.priority);
    setFlagged(reminder.flagged);
  }, [reminder]);

  const handleSave = () => {
    onUpdate(reminder.id, {
      title,
      notes: notes || undefined,
      url: url || undefined,
      remindAt: remindAt || undefined,
      priority,
      flagged,
    });
  };

  return (
    <div
      className="w-80 h-full border-l overflow-y-auto shrink-0"
      style={{ background: "var(--bg-secondary)", borderColor: "var(--border-color)" }}
    >
      <div className="flex items-center justify-between p-4 border-b" style={{ borderColor: "var(--border-color)" }}>
        <h3 className="text-sm font-semibold">Details</h3>
        <button
          onClick={onClose}
          className="text-sm px-2 py-0.5 rounded hover:bg-[var(--border-color)]"
          style={{ color: "var(--apple-blue)" }}
        >
          Done
        </button>
      </div>

      <div className="p-4 space-y-4">
        <div>
          <label className="text-xs font-medium" style={{ color: "var(--text-secondary)" }}>Title</label>
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            onBlur={handleSave}
            className="w-full mt-1 px-3 py-2 text-sm rounded-lg border outline-none focus:ring-2 focus:ring-[var(--apple-blue)]"
            style={{
              background: "var(--bg-primary)",
              borderColor: "var(--border-color)",
              color: "var(--text-primary)",
            }}
          />
        </div>

        <div>
          <label className="text-xs font-medium" style={{ color: "var(--text-secondary)" }}>Notes</label>
          <textarea
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            onBlur={handleSave}
            rows={3}
            className="w-full mt-1 px-3 py-2 text-sm rounded-lg border outline-none resize-none focus:ring-2 focus:ring-[var(--apple-blue)]"
            style={{
              background: "var(--bg-primary)",
              borderColor: "var(--border-color)",
              color: "var(--text-primary)",
            }}
          />
        </div>

        <div>
          <label className="text-xs font-medium" style={{ color: "var(--text-secondary)" }}>URL</label>
          <input
            value={url}
            onChange={(e) => setUrl(e.target.value)}
            onBlur={handleSave}
            placeholder="https://"
            className="w-full mt-1 px-3 py-2 text-sm rounded-lg border outline-none focus:ring-2 focus:ring-[var(--apple-blue)]"
            style={{
              background: "var(--bg-primary)",
              borderColor: "var(--border-color)",
              color: "var(--text-primary)",
            }}
          />
        </div>

        <div>
          <label className="text-xs font-medium" style={{ color: "var(--text-secondary)" }}>Date & Time</label>
          <input
            type="datetime-local"
            value={remindAt}
            onChange={(e) => {
              setRemindAt(e.target.value);
            }}
            onBlur={handleSave}
            className="w-full mt-1 px-3 py-2 text-sm rounded-lg border outline-none focus:ring-2 focus:ring-[var(--apple-blue)]"
            style={{
              background: "var(--bg-primary)",
              borderColor: "var(--border-color)",
              color: "var(--text-primary)",
            }}
          />
        </div>

        <div>
          <label className="text-xs font-medium" style={{ color: "var(--text-secondary)" }}>Priority</label>
          <div className="flex gap-1 mt-1">
            {Object.values(Priority).map((p) => (
              <button
                key={p}
                onClick={() => {
                  setPriority(p);
                  setTimeout(handleSave, 0);
                }}
                className={`flex-1 py-1.5 text-xs font-medium rounded-md border transition-colors ${
                  priority === p ? "text-white" : ""
                }`}
                style={{
                  borderColor: "var(--border-color)",
                  background: priority === p ? "var(--apple-blue)" : "var(--bg-primary)",
                  color: priority === p ? "white" : "var(--text-primary)",
                }}
              >
                {p === Priority.NONE ? "None" : p.charAt(0) + p.slice(1).toLowerCase()}
              </button>
            ))}
          </div>
        </div>

        <div className="flex items-center justify-between">
          <label className="text-xs font-medium" style={{ color: "var(--text-secondary)" }}>Flagged</label>
          <button
            onClick={() => {
              setFlagged(!flagged);
              setTimeout(handleSave, 0);
            }}
            className="text-xl"
            style={{ color: flagged ? "var(--apple-orange)" : "var(--text-secondary)" }}
          >
            {flagged ? "⚑" : "⚐"}
          </button>
        </div>
      </div>
    </div>
  );
}
