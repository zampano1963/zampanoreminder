export enum Priority {
  NONE = "NONE",
  LOW = "LOW",
  MEDIUM = "MEDIUM",
  HIGH = "HIGH",
}

export interface Reminder {
  id: number;
  title: string;
  notes: string | null;
  url: string | null;
  remindAt: string | null;
  priority: Priority;
  flagged: boolean;
  completed: boolean;
  sortOrder: number;
  listId: number | null;
  listName: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface ReminderRequest {
  title: string;
  notes?: string;
  url?: string;
  remindAt?: string;
  priority?: Priority;
  flagged?: boolean;
  listId?: number;
}

export interface ReminderList {
  id: number;
  name: string;
  color: string;
  icon: string | null;
  sortOrder: number;
  deletable: boolean;
  reminderCount: number;
  createdAt: string;
  updatedAt: string;
}
