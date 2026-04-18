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
  const [error, setError] = useState<string | null>(null);

  const fetchReminders = useCallback(async () => {
    try {
      setError(null);
      const data = await api.get<Reminder[]>("/api/reminders");
      setReminders(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load reminders");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchReminders();
  }, [fetchReminders]);

  const handleAdd = async (title: string) => {
    try {
      setError(null);
      const created = await api.post<Reminder>("/api/reminders", { title });
      setReminders((prev) => [...prev, created]);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to create reminder");
    }
  };

  const handleToggle = async (id: number) => {
    const original = reminders;
    setReminders((prev) =>
      prev.map((r) => (r.id === id ? { ...r, completed: !r.completed } : r))
    );
    try {
      const updated = await api.patch<Reminder>(`/api/reminders/${id}/toggle`);
      setReminders((prev) => prev.map((r) => (r.id === id ? updated : r)));
      if (selected?.id === id) setSelected(updated);
    } catch (err) {
      setReminders(original);
      setError(err instanceof Error ? err.message : "Failed to toggle reminder");
    }
  };

  const handleDelete = async (id: number) => {
    const original = reminders;
    setReminders((prev) => prev.filter((r) => r.id !== id));
    if (selected?.id === id) setSelected(null);
    try {
      await api.delete(`/api/reminders/${id}`);
    } catch (err) {
      setReminders(original);
      setError(err instanceof Error ? err.message : "Failed to delete reminder");
    }
  };

  const handleUpdate = async (id: number, data: ReminderRequest) => {
    try {
      setError(null);
      const updated = await api.put<Reminder>(`/api/reminders/${id}`, data);
      setReminders((prev) => prev.map((r) => (r.id === id ? updated : r)));
      setSelected(updated);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to update reminder");
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

          {error && (
            <div
              className="mb-4 px-4 py-2.5 rounded-lg text-sm flex items-center justify-between"
              style={{ background: "var(--apple-red)", color: "white" }}
            >
              <span>{error}</span>
              <button onClick={() => setError(null)} className="ml-2 font-bold">✕</button>
            </div>
          )}

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

          {incomplete.length === 0 && !error && (
            <div className="text-center py-12">
              <p className="text-lg mb-1" style={{ color: "var(--text-secondary)" }}>No reminders</p>
              <p className="text-sm" style={{ color: "var(--text-secondary)" }}>
                Tap + to create your first reminder
              </p>
            </div>
          )}

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
