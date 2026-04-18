"use client";

import { useState, useEffect, useCallback } from "react";
import { Reminder, ReminderRequest } from "@/types/reminder";
import { api } from "@/lib/api";
import ReminderItem from "./ReminderItem";
import ReminderDetail from "./ReminderDetail";
import AddReminder from "./AddReminder";

export default function ReminderListView() {
  const [reminders, setReminders] = useState<Reminder[]>([]);
  const [selected, setSelected] = useState<Reminder | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchReminders = useCallback(async () => {
    try {
      const data = await api.get<Reminder[]>("/api/reminders");
      setReminders(data);
    } catch (err) {
      console.error("Failed to fetch reminders:", err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchReminders();
  }, [fetchReminders]);

  const handleAdd = async (title: string) => {
    try {
      const created = await api.post<Reminder>("/api/reminders", { title });
      setReminders((prev) => [...prev, created]);
    } catch (err) {
      console.error("Failed to create reminder:", err);
    }
  };

  const handleToggle = async (id: number) => {
    try {
      const updated = await api.patch<Reminder>(`/api/reminders/${id}/toggle`);
      setReminders((prev) => prev.map((r) => (r.id === id ? updated : r)));
      if (selected?.id === id) setSelected(updated);
    } catch (err) {
      console.error("Failed to toggle reminder:", err);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await api.delete(`/api/reminders/${id}`);
      setReminders((prev) => prev.filter((r) => r.id !== id));
      if (selected?.id === id) setSelected(null);
    } catch (err) {
      console.error("Failed to delete reminder:", err);
    }
  };

  const handleUpdate = async (id: number, data: ReminderRequest) => {
    try {
      const updated = await api.put<Reminder>(`/api/reminders/${id}`, data);
      setReminders((prev) => prev.map((r) => (r.id === id ? updated : r)));
      setSelected(updated);
    } catch (err) {
      console.error("Failed to update reminder:", err);
    }
  };

  const incomplete = reminders.filter((r) => !r.completed);
  const completed = reminders.filter((r) => r.completed);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-full">
        <p className="text-sm" style={{ color: "var(--text-secondary)" }}>Loading...</p>
      </div>
    );
  }

  return (
    <div className="flex h-full">
      <div className="flex-1 overflow-y-auto">
        <div className="p-6">
          <h1 className="text-2xl font-bold mb-1" style={{ color: "var(--apple-blue)" }}>
            All Reminders
          </h1>
          <p className="text-sm mb-4" style={{ color: "var(--text-secondary)" }}>
            {incomplete.length} remaining
          </p>

          <div className="space-y-0.5">
            {incomplete.map((r) => (
              <ReminderItem
                key={r.id}
                reminder={r}
                selected={selected?.id === r.id}
                onSelect={setSelected}
                onToggle={handleToggle}
                onDelete={handleDelete}
              />
            ))}
          </div>

          <div className="mt-2">
            <AddReminder onAdd={handleAdd} />
          </div>

          {completed.length > 0 && (
            <div className="mt-6">
              <h2
                className="text-xs font-semibold uppercase tracking-wider mb-2 px-4"
                style={{ color: "var(--text-secondary)" }}
              >
                Completed ({completed.length})
              </h2>
              <div className="space-y-0.5">
                {completed.map((r) => (
                  <ReminderItem
                    key={r.id}
                    reminder={r}
                    selected={selected?.id === r.id}
                    onSelect={setSelected}
                    onToggle={handleToggle}
                    onDelete={handleDelete}
                  />
                ))}
              </div>
            </div>
          )}
        </div>
      </div>

      {selected && (
        <ReminderDetail
          reminder={selected}
          onUpdate={handleUpdate}
          onClose={() => setSelected(null)}
        />
      )}
    </div>
  );
}
