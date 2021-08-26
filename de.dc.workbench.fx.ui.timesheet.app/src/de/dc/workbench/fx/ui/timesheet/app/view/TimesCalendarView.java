package de.dc.workbench.fx.ui.timesheet.app.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.util.URI;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import de.dc.workbench.fx.core.command.di.EmfFXPlatform;
import de.dc.workbench.fx.core.event.EventContext;
import de.dc.workbench.fx.core.event.EventTopic;
import de.dc.workbench.fx.core.event.IEventBroker;
import de.dc.workbench.fx.core.model.EmfOutlineContext;
import de.dc.workbench.fx.core.xtext.IXtextService;
import de.dc.workbench.fx.ui.EmfView;
import de.dc.workbench.fx.ui.monaco.MonacoTextEditor;
import de.dc.workbench.fx.ui.timesheet.DayModel;
import de.dc.workbench.fx.ui.timesheet.MonthModel;
import de.dc.workbench.fx.ui.timesheet.Timesheet;
import de.dc.workbench.fx.ui.timesheet.TrackingData;
import de.dc.workbench.fx.ui.timesheet.WeekModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

public class TimesCalendarView extends EmfView{

	@Inject IXtextService xtextService;
	@Inject IEventBroker eventBroker;
	
	private CalendarView calendarView = new CalendarView();
	private CalendarSource source;
	
	public TimesCalendarView() {
		super("Times Calendar");

		EmfFXPlatform.inject(this);
		
		eventBroker.register(this);
		
		var parent = new BorderPane();
		calendarView.setShowSearchField(true);
		calendarView.showWeekPage();
		source = new CalendarSource("Standard");
		calendarView.getCalendarSources().setAll(source);
		parent.setCenter(calendarView);

		var scrollPane = new ScrollPane(parent);
		scrollPane.setFitToWidth(true);
		scrollPane.setPannable(true);
		setContent(scrollPane);
	}
	
	@Subscribe
	public void subscribeMonacoEditorTextChanged(EventContext<EmfOutlineContext> context) throws IOException {
		if (context.match("/monaco/editor/text/changed")) {
			updateCalendarByMonaco(context.getInput());
		}
	}
	
	@Subscribe
	public void subscribeCurrentSelectedEditor(EventContext<Tab> context) {
		if (context.match(EventTopic.CURRENT_SELECTED_EDITOR) && context.getInput() instanceof MonacoTextEditor editor) {
			updateCalendarByFile(editor.getInput().getFile());
		}
	}

	private void updateCalendarByFile(String fileName, String content) {
		try {
			var resourceSet = xtextService.getResourceSetByExtension(fileName);
			var resource = resourceSet.createResource(URI.createURI("dummy:/example.spelltimesheet"));
			var in = new ByteArrayInputStream(content.getBytes());
			resource.load(in, resourceSet.getLoadOptions());
			var eObject = resource.getContents().get(0);
			var timesheet = (Timesheet) eObject;
			updateCalendar(timesheet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateCalendarByMonaco(EmfOutlineContext input) {
		updateCalendarByFile(input.getFile().getAbsolutePath(), input.getText());
	}
	
	private void updateCalendarByFile(File file) {
		try {
			updateCalendarByFile(file.getAbsolutePath(), FileUtils.readFileToString(file, StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, Calendar> calendars = new TreeMap<>();
	
	private void updateCalendar(Timesheet timesheet) {
		source.getCalendars().clear();
		calendars.clear();
		
		for(MonthModel month : timesheet.getMonths()) {
			for (WeekModel week : month.getWeeks()) {
				for (DayModel day : week.getDays()) {
					for (TrackingData data : day.getDatas()) {
						var calendar = getCalendar(data.getProjectKey());
						var entry = new Entry<>(data.getProjectKey()+" | "+data.getDescription());
						var date = day.getDate();
						if (date!=null) {
							LocalTime start = data.getStart();
							LocalTime end = data.getEnd();
							if(start!=null && end!=null) {
								entry.setInterval(date.atTime(start), date.atTime(end));
								calendar.addEntries(entry);
							}
						}
					}
				}
			}
		}
	}

	private Calendar getCalendar(String projectKey) {
		if (calendars.get(projectKey)==null) {
			var calendar = new Calendar();
			calendar.setName(projectKey);
			source.getCalendars().addAll(calendar);
			calendars.put(projectKey, calendar);
			
			var arrayList = new ArrayList<>(calendars.keySet());
			var indexOf = arrayList.indexOf(projectKey);
			calendar.setStyle("style"+indexOf);
		}
		
		return calendars.get(projectKey);
	}
	
}
