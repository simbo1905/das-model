package com.github.simbo1905.dasmodel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.CellVisitor;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellAreaEvent;
import org.zkoss.zss.ui.event.Events;
import org.zkoss.zss.ui.event.StopEditingEvent;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller listens all Spreadsheet's events and show related messages.
 *
 * @author dennis, Hawk
 */
public class DASModelComposer extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    private ListModelList<String> eventFilterModel = new ListModelList<String>();
    private ListModelList<String> infoModel = new ListModelList<String>();

    @Wire
    Spreadsheet ss;
    @Wire
    private Listbox eventFilterList;
    @Wire
    private Grid infoList;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        initModel();
    }

    @Listen("onAfterCellChange = #ss")
    public void onAfterCellChange(CellAreaEvent event) {
        StringBuilder info = new StringBuilder();

        Range changed = Ranges.range(event.getSheet(), event.getArea());

        Range aRange = Ranges.rangeByName(ss.getSelectedSheet(), "APPRENTICES");

        int row = event.getArea().getRow();
        int col = event.getArea().getColumn();

        int aRowMin = aRange.getRow();
        int aRowMax = aRange.getLastRow();
        int aColMin = aRange.getColumn();
        int aColMax = aRange.getLastColumn();

        if( row >= aRowMin && row <= aRowMax) {
            if( col >= aColMin && col <= aColMax ) {
                addInfo(String.format("apprentice changed %s,%s", row, col));
                recompute();
            }
        }
    }

    private void initModel() {

        eventFilterModel.setMultiple(true);

        addEventFilter(Events.ON_AFTER_CELL_CHANGE, true);

        eventFilterList.setModel(eventFilterModel);

        //add default show only
        infoList.setModel(infoModel);
        addInfo("Spreadsheet initialized");

        recompute();
    }

    void recompute() {

        addInfo("recompute");

        List<Range> trainingDueCells = new ArrayList<>();

        List<Cell> apprenticeCells = new ArrayList<Cell>();

        // dig out the cells for the apprentices
        Range aRange = Ranges.rangeByName(ss.getSelectedSheet(), "APPRENTICES");
        aRange.visit(new CellVisitor() {
            @Override
            public boolean ignoreIfNotExist(int row, int column) {
                return true; // do ignore if not exist
            }

            @Override
            @SuppressWarnings("deprecation")
            public boolean createIfNotExist(int row, int column) {
                return false; // don't create
            }

            @Override
            public boolean visit(Range cellRange) {
                int row = cellRange.getRow();
                int col = cellRange.getColumn();
                Cell cell = new Cell(row, col, Double.valueOf(cellRange.getCellValue().toString()));
                apprenticeCells.add(cell);
                return true; // keep visiting
            }
        });

        // dig out the cells for the training due
        Range trainingDue = Ranges.rangeByName(ss.getSelectedSheet(), "TRAINING_DUE");
        trainingDue.visit(new CellVisitor() {
            @Override
            public boolean ignoreIfNotExist(int row, int column) {
                return true;
            }

            @Override
            @SuppressWarnings("deprecation")
            public boolean createIfNotExist(int row, int column) {
                return false;
            }

            @Override
            public boolean visit(Range cellRange) {
                trainingDueCells.add(cellRange);
                return true; // keep visiting
            }
        });

        Range bRange = Ranges.rangeByName(ss.getSelectedSheet(), "BUDGET");
        Object b = bRange.getCellValue();
        double budget = (b == null) ? 0.0 : Double.valueOf(b.toString());
        addInfo("budget is " + budget);

        Range cfRange = Ranges.rangeByName(ss.getSelectedSheet(), "CO_RATIO");
        Object cf = cfRange.getCellValue();
        double cofund = (cf == null) ? 0.0 : Double.valueOf(cf.toString());
        addInfo("cofund ratio is " + cofund);

        // convert the apprentices cells into a set of apprentices
        List<Apprentice> apprentices = DASModel$.MODULE$.cellsToApprentices(apprenticeCells);

        for (Apprentice a : apprentices) {
            addInfo(a.toString());
        }

        List<TrainingDue> trainingDues = DASModel$.MODULE$.apprenticesToTrainingDue(apprentices);

        for (int i = 0; i < 12; i++) {
            Double amount = trainingDues.get(i).amount();
            addInfo(String.format("%s is %s", i, amount));
            trainingDueCells.get(i).setCellValue(amount);
        }
    }

    private void addEventFilter(String event, boolean showinfo) {
        if (!eventFilterModel.contains(eventFilterModel)) {
            eventFilterModel.add(event);
        }
        if (showinfo) {
            eventFilterModel.addToSelection(event);
        } else {
            eventFilterModel.removeFromSelection(event);
        }
    }

    private boolean isShowEventInfo(String event) {
        return eventFilterModel.getSelection().contains(event);
    }

    @Listen("onClick = #clearAllFilter")
    public void onClearAllFilter() {
        eventFilterModel.clearSelection();
    }

    @Listen("onClick = #selectAllFilter")
    public void onSelectAll() {
        eventFilterModel.clearSelection();
        eventFilterModel.setSelection(new ArrayList<String>(eventFilterModel));
    }

    private void addInfo(String info) {
        infoModel.add(0, info);
        while (infoModel.size() > 100) {
            infoModel.remove(infoModel.size() - 1);
        }
    }

    @Listen("onClick = #clearInfo")
    public void onClearInfo() {
        infoModel.clear();
    }

}



