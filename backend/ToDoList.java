package backend;

import java.time.*;
import java.util.*;

public class ToDoList {
	private List<ActionItem> incompleteItems = new ArrayList<ActionItem>();
	private List<ActionItem> completeItems = new ArrayList<ActionItem>();

	public ActionItem getIncompleteItemAtIndex(int index) {
		return incompleteItems.get(index);
	}

	public int getNumIncompleteItems() {
		return incompleteItems.size();
	}

	public ActionItem getCompleteItemAtIndex(int index) {
		return completeItems.get(index);
	}

	public int getNumCompleteItems() {
		return completeItems.size();
	}

	public void moveActionItem(int oldIndex, int newIndex) {
		ActionItem item = incompleteItems.get(oldIndex);
		incompleteItems.remove(item);
		incompleteItems.add(newIndex, item);
		if (oldIndex < newIndex) {
			if (item.getPriority() == Priority.INACTIVE
					&& incompleteItems.get(newIndex - 1)
							.getPriority() == Priority.INACTIVE
					&& item.getActiveByDate() != incompleteItems
							.get(newIndex - 1).getActiveByDate()) {
				item.setEventualByDate(
						incompleteItems.get(newIndex - 1).getActiveByDate());
				item.updateActionItem(item.getTitle(), item.getPriority(),
						item.getUrgentByDate(), item.getCurrentByDate(),
						incompleteItems.get(newIndex - 1).getActiveByDate(),
						item.getComment());
			}
			if (item.getPriority() != incompleteItems.get(newIndex - 1)
					.getPriority()) {
				if (incompleteItems.get(newIndex - 1)
						.getPriority() == Priority.INACTIVE) {

				}
				item.updateActionItem(item.getTitle(),
						incompleteItems.get(newIndex - 1).getPriority(),
						item.getUrgentByDate(), item.getCurrentByDate(),
						item.getEventualByDate(), item.getComment());
			}
		} else {
			if (item.getPriority() == Priority.INACTIVE
					&& incompleteItems.get(newIndex + 1)
							.getPriority() == Priority.INACTIVE
					&& item.getActiveByDate() != incompleteItems
							.get(newIndex + 1).getActiveByDate()) {
				item.setEventualByDate(
						incompleteItems.get(newIndex + 1).getActiveByDate());
				item.updateActionItem(item.getTitle(), item.getPriority(),
						item.getUrgentByDate(), item.getCurrentByDate(),
						incompleteItems.get(newIndex + 1).getActiveByDate(),
						item.getComment());
			}
			if (item.getPriority() != incompleteItems.get(newIndex + 1)
					.getPriority()) {
				if (incompleteItems.get(newIndex + 1)
						.getPriority() == Priority.INACTIVE) {
					item.setEventualByDate(incompleteItems.get(newIndex + 1)
							.getActiveByDate());
				}
				item.updateActionItem(item.getTitle(),
						incompleteItems.get(newIndex + 1).getPriority(),
						item.getUrgentByDate(), item.getCurrentByDate(),
						item.getEventualByDate(), item.getComment());
			}
		}
	}

	public void updateListOrder() {
		List<ActionItem> incompleteItems = new ArrayList<ActionItem>();
		List<ActionItem> urgent = new ArrayList<ActionItem>();
		List<ActionItem> current = new ArrayList<ActionItem>();
		List<ActionItem> eventual = new ArrayList<ActionItem>();
		List<ActionItem> unsortedInactive = new ArrayList<ActionItem>();
		List<ActionItem> inactive = new ArrayList<ActionItem>();
		List<ActionItem> completed = new ArrayList<ActionItem>();
		for (int i = 0; i < this.incompleteItems.size(); i++) {
			this.incompleteItems.get(i).updatePriority();
			if (this.incompleteItems.get(i).getPriority() == Priority.URGENT) {
				urgent.add(this.incompleteItems.get(i));
			}
			else if (this.incompleteItems.get(i).getPriority() == Priority.CURRENT) {
				current.add(this.incompleteItems.get(i));
			}
			else if (this.incompleteItems.get(i).getPriority() == Priority.EVENTUAL) {
				eventual.add(this.incompleteItems.get(i));
			}
			else if (this.incompleteItems.get(i).getPriority() == Priority.INACTIVE) {
				unsortedInactive.add(this.incompleteItems.get(i));
			}
			else if (this.incompleteItems.get(i).getPriority() == Priority.COMPLETED) {
				completed.add(this.incompleteItems.get(i));
			}
		}
		for (int i=0;i<completed.size();i++) {
			completeActionItem(completed.get(i));
		}
		while (unsortedInactive.size() > 0) {
			ActionItem earliest = unsortedInactive.get(0);
			for (int i = 1; i < unsortedInactive.size(); i++) {
				if (unsortedInactive.get(i).getActiveByDate() != null && earliest.getActiveByDate() == null) {	
					earliest = unsortedInactive.get(i);
				} else if (unsortedInactive.get(i).getActiveByDate() != null && earliest.getActiveByDate() != null) {
					if (unsortedInactive.get(i).getActiveByDate()
							.isBefore(earliest.getActiveByDate()))
						earliest = unsortedInactive.get(i);
				}
			}
			unsortedInactive.remove(earliest);
			inactive.add(earliest);
		}
		for (ActionItem item : urgent) {
			incompleteItems.add(item);
		}
		for (ActionItem item : current) {
			incompleteItems.add(item);
		}
		for (ActionItem item : eventual) {
			incompleteItems.add(item);
		}
		for (ActionItem item : inactive) {
			incompleteItems.add(item);
		}
		this.incompleteItems = incompleteItems;
	}
	public void undoCompleteActionItem(ActionItem item) {
		if (item.getPriority() == Priority.COMPLETED)
			item.updateActionItem(item.getTitle(), Priority.URGENT,
					item.getUrgentByDate(), item.getCurrentByDate(),
					item.getEventualByDate(), item.getComment());
		completeItems.remove(item);
		incompleteItems.add(item);
	}
	public void completeActionItem(int index) {
		ActionItem item = incompleteItems.get(index);
		item.setCompletedByDate(LocalDateTime.now());
		item.updateActionItem(item.getTitle(), Priority.COMPLETED,
				item.getUrgentByDate(), item.getCurrentByDate(),
				item.getEventualByDate(), item.getComment());
		completeItems.add(item);
		incompleteItems.remove(index);
	}

	public void completeActionItem(ActionItem item) {
		item.setCompletedByDate(LocalDateTime.now());
		item.updateActionItem(item.getTitle(), Priority.COMPLETED,
				item.getUrgentByDate(), item.getCurrentByDate(),
				item.getEventualByDate(), item.getComment());
		completeItems.add(item);
		incompleteItems.remove(item);
	}

	public void deleteActionItem(int index) {
		incompleteItems.remove(index);
	}

	public void deleteActionItem(ActionItem item) {
		incompleteItems.remove(item);
		completeItems.remove(item);
	}

	public void addActionItem(ActionItem item) {
		// item.setPriority(Priority.URGENT);
		incompleteItems.add(0, item);
	}

	// this is for restoring from a backup
	public void addCompleteActionItem(ActionItem item) {
		completeItems.add(item);
	}
	public static void main(String[] arg) {
		ToDoList list = new ToDoList();
		for (int i = 0; i < 5; i++) {
			ActionItem item1 = new ActionItem();
			item1.setPriority(Priority.URGENT);
			ActionItem item2 = new ActionItem();
			item2.setPriority(Priority.CURRENT);
			ActionItem item3 = new ActionItem();
			item3.setPriority(Priority.EVENTUAL);
			list.addActionItem(item1);
			list.addActionItem(item2);
			list.addActionItem(item3);
		}
		list.updateListOrder();
		for (int i = 0; i < list.getNumIncompleteItems(); i++) {
			System.out.println(list.getIncompleteItemAtIndex(i).getPriority());
		}
	}
}
