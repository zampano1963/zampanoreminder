import AppLayout from "@/components/layout/AppLayout";
import ReminderListView from "@/components/reminder/ReminderListView";

export default function Home() {
  return (
    <AppLayout>
      <ReminderListView />
    </AppLayout>
  );
}
