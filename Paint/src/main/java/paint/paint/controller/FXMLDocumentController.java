package paint.paint.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.xml.sax.SAXException;
import paint.paint.model.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FXMLDocumentController implements Initializable, DrawingEngine {

    /***FXML VARIABLES***/
    @FXML
    private Button DeleteBtn;

    @FXML
    private ComboBox<String> ShapeBox;

    @FXML
    private Button UndoBtn;

    @FXML
    private Button RedoBtn;

    @FXML
    private ColorPicker ColorBox1;

    @FXML
    private ColorPicker ColorBox2;

    @FXML
    private Label Coords;

    @FXML
    private Button SaveBtn;

    @FXML
    private Button MoveBtn;

    @FXML
    private Button RecolorBtn;

    @FXML
    private Button LoadBtn;

    @FXML
    private GridPane After;

    @FXML
    private Pane Before;

    @FXML
    private Pane PathPane;

    @FXML
    private TextField PathText;

    @FXML
    private Button StartBtn;

    @FXML
    private Button ResizeBtn;

    @FXML
    private Button ImportBtn;

    @FXML
    private Button PathBtn;

    @FXML
    private Canvas CanvasBox;

    @FXML
    private Button CopyBtn;

    @FXML
    private Label Message;

    @FXML
    private ListView ShapeList;

    @FXML
    private Slider WidthSlider;


    /***CLASS VARIABLES***/
    private Point2D start;
    private Point2D end;

    //SINGLETON DP
    private static ArrayList<Shape> shapeList = new ArrayList<Shape>();

    private boolean move = false;
    private boolean copy = false;
    private boolean resize = false;
    private boolean save = false;
    private boolean load = false;
    private boolean importt = false;

    //MEMENTO DP
    private final Stack primary = new Stack<ArrayList<Shape>>();
    private final Stack secondary = new Stack<ArrayList<Shape>>();

    private Shape predrawShape;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == StartBtn) {
            Before.setVisible(false);
            After.setVisible(true);
        }

        Message.setText("");
        if (event.getSource() == DeleteBtn) {
            if (!ShapeList.getSelectionModel().isEmpty()) {
                int index = ShapeList.getSelectionModel().getSelectedIndex();
                removeShape(shapeList.get(index));
            } else {
                Message.setText("Вы должны выбрать фигуру перед удалением.");
            }
        }

        if (event.getSource() == RecolorBtn) {
            if (!ShapeList.getSelectionModel().isEmpty()) {
                int index = ShapeList.getSelectionModel().getSelectedIndex();
                shapeList.get(index).setFillColor(ColorBox2.getValue());
                refresh(CanvasBox);
            } else {
                Message.setText("Вы должны выбрать фигуру перед заливкой.");
            }
        }

        if (event.getSource() == MoveBtn) {
            if (!ShapeList.getSelectionModel().isEmpty()) {
                move = true;
                Message.setText("Нажмите в точку для задания верхней-левой точки выбранной фигуры.");
            } else {
                Message.setText("Вы должны выбрать фигуру перед перемещением.");
            }
        }

        if (event.getSource() == CopyBtn) {
            if (!ShapeList.getSelectionModel().isEmpty()) {
                copy = true;
                Message.setText("Нажмите в точку для задания верхней-левой точки новой фигуры.");
            } else {
                Message.setText("Вы должны выбрать фигуру перед копированием.");
            }
        }

        if (event.getSource() == ResizeBtn) {
            if (!ShapeList.getSelectionModel().isEmpty()) {
                resize = true;
                Message.setText("Нажмите в точку для задания новой правой-нижней точки выбранной фигуры.");
            } else {
                Message.setText("Вы должны выбрать фигуру перед изменением размера.");
            }
        }

        if (event.getSource() == UndoBtn) {
            if (primary.empty()) {
                Message.setText("Вернулись в ноль! .. Нельзя сделать Undo больше!");
                return;
            }
            undo();
        }

        if (event.getSource() == RedoBtn) {
            if (secondary.empty()) {
                Message.setText("История рисования закончилась .. нарисуйте еще.");
                return;
            }
            redo();
        }

        if (event.getSource() == SaveBtn) {
            showPathPane();
            save = true;
        }

        if (event.getSource() == LoadBtn) {
            showPathPane();
            load = true;
        }

        if (event.getSource() == ImportBtn) {
            showPathPane();
            importt = true;
        }

        if (event.getSource() == PathBtn) {
            if (PathText.getText().isEmpty()) {
                PathText.setText("Вы должны указать путь к файлу!.");
                return;
            }
            if (save) {
                save = false;
                save(PathText.getText());
            } else if (load) {
                load = false;
                load(PathText.getText());
            } else if (importt) {
                importt = false;
                installPluginShape(PathText.getText());
            }
            hidePathPane();
        }
    }

    public void showCoords(MouseEvent event) {
        Coords.setText("x: " + event.getX() + "; y: " + event.getY());
    }

    public void showPathPane() {
        Message.setVisible(false);
        PathPane.setVisible(true);
    }

    public void hidePathPane() {
        PathPane.setVisible(false);
        Message.setVisible(true);
    }

    public void startDrag(MouseEvent event) {
        start = new Point2D(event.getX(), event.getY());
        Message.setText("");
    }

    public void endDrag(MouseEvent event) throws CloneNotSupportedException {
        end = new Point2D(event.getX(), event.getY());
        if (end.equals(start)) {
            clickFunction();
        } else {
            dragFunction();
        }
    }

    public void clickFunction() throws CloneNotSupportedException {
        if (move) {
            move = false;
            moveFunction();
        } else if (copy) {
            copy = false;
            copyFunction();
        } else if (resize) {
            resize = false;
            resizeFunction();
        }
    }

    public void moveFunction() {
        int index = ShapeList.getSelectionModel().getSelectedIndex();
        shapeList.get(index).setTopLeft(start);
        refresh(CanvasBox);
    }

    public void copyFunction() throws CloneNotSupportedException {
        int index = ShapeList.getSelectionModel().getSelectedIndex();
        Shape temp = shapeList.get(index).cloneShape();
        if (temp.equals(null)) {
            System.out.println("Ошибка клонирования!");
        } else {
            shapeList.add(temp);
            shapeList.get(shapeList.size() - 1).setTopLeft(start);
            refresh(CanvasBox);
        }
    }

    public void resizeFunction() {
        int index = ShapeList.getSelectionModel().getSelectedIndex();
        Color c = shapeList.get(index).getFillColor();
        start = shapeList.get(index).getTopLeft();
        //Factory DP
        Shape temp = new ShapeFactory().create(shapeList.get(index).getClass().getSimpleName(), start, end,
                ColorBox1.getValue(), ColorBox2.getValue(), (int) WidthSlider.getValue());
        if (temp.getClass().getSimpleName().equals("Line")) {
            Message.setText("Line не поддерживает изменение размера :(");
            return;
        }
        shapeList.remove(index);
        temp.setFillColor(c);
        shapeList.add(index, temp);
        refresh(CanvasBox);

    }

    public void predragFunction(MouseEvent event) throws CloneNotSupportedException {
        end = new Point2D(event.getX(), event.getY());
        if (end.equals(start)) {
            clickFunction();
        } else {
            redraw(CanvasBox);
            String type = ShapeBox.getValue();
            try {
                predrawShape = new ShapeFactory().create(type, start, end,
                        ColorBox1.getValue(), ColorBox2.getValue(), (int) WidthSlider.getValue());
            } catch (Exception e) {
                Message.setText("Сначала выберите фигуру для рисования :)");
                return;
            }
            predrawShape.draw(CanvasBox);
        }
    }

    public void dragFunction() {
        String type = ShapeBox.getValue();
        Shape sh;
        //Factory DP
        try {
            sh = new ShapeFactory().create(type, start, end,
                    ColorBox1.getValue(), ColorBox2.getValue(), (int) WidthSlider.getValue());
        } catch (Exception e) {
            Message.setText("Сначала выберите фигуру для рисования :)");
            return;
        }
        addShape(sh);
        sh.draw(CanvasBox);

    }


    //Observer DP
    public ObservableList getStringList() {
        ObservableList l = FXCollections.observableArrayList(new ArrayList());
        try {
            for (int i = 0; i < shapeList.size(); i++) {
                String temp = shapeList.get(i).getClass().getSimpleName() + "  (" + (int)
                        shapeList.get(i).getTopLeft().getX() + "," + (int) shapeList.get(i).getTopLeft().getY() + ")";
                l.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public ArrayList<Shape> cloneList(ArrayList<Shape> l) throws CloneNotSupportedException {
        ArrayList<Shape> temp = new ArrayList<Shape>();
        for (int i = 0; i < l.size(); i++) {
            temp.add(l.get(i).cloneShape());
        }
        return temp;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList shapeList = FXCollections.observableArrayList();

        List<Class<Shape>> shapeClasses = ShapeFactory.getActualFigures();
        for (Class shapeClass : shapeClasses) {
            String name = shapeClass.getName().substring(25);
            shapeList.add(name);
        }

        ShapeBox.setItems(shapeList);

        ColorBox1.setValue(Color.YELLOW);
        ColorBox2.setValue(Color.BLUE);
    }

    @Override
    public void refresh(Object canvas) {
        try {
            primary.push(new ArrayList(cloneList(shapeList)));
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        redraw((Canvas) canvas);
        ShapeList.setItems((getStringList()));
    }

    public void redraw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 850, 370);
        try {
            for (int i = 0; i < shapeList.size(); i++) {
                shapeList.get(i).draw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addShape(Shape shape) {
        shapeList.add(shape);
        refresh(CanvasBox);
    }

    @Override
    public void removeShape(Shape shape) {
        shapeList.remove(shape);
        refresh(CanvasBox);
    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) {
        shapeList.remove(oldShape);
        shapeList.add(newShape);
        refresh(CanvasBox);
    }

    @Override
    public Shape[] getShapes() {
        return (Shape[]) shapeList.toArray();
    }

    @Override
    public void undo() {
        if (secondary.size() < 21) {
            ArrayList temp = (ArrayList) primary.pop();
            secondary.push(temp);

            if (primary.empty()) {
                shapeList = new ArrayList();
            } else {
                temp = (ArrayList) primary.peek();
                shapeList = temp;
            }

            redraw(CanvasBox);
            ShapeList.setItems((getStringList()));
        } else {
            Message.setText("Нельзя сделать больше 20 отмен действий :'(");
        }
    }

    @Override
    public void redo() {
        ArrayList temp = (ArrayList) secondary.pop();
        primary.push(temp);

        temp = (ArrayList) primary.peek();
        shapeList = temp;

        redraw(CanvasBox);
        ShapeList.setItems((getStringList()));
    }

    @Override
    public void save(String path) {
        if (path.endsWith(".xml")) {
            SaveToXML x = new SaveToXML(path, shapeList);
            if (x.checkSuccess()) {
                Message.setText("Файл успешно сохранен!");
            } else {
                Message.setText("Ошибка во время сохраниения, проверьте путь и попробуйте снова!");
            }
        } else if (path.endsWith(".json")) {
            Message.setText("Json еще не поддерживается :(");
        } else {
            Message.setText("Неправильный формат файла .. сохраните в .xml или .json");
        }

    }

    @Override
    public void load(String path) {
        if (path.endsWith(".xml")) {
            try {
                LoadFromXML l = new LoadFromXML(path);
                if (l.checkSuccess()) {
                    shapeList = l.getList();
                    refresh(CanvasBox);
                    Message.setText("Файл загружен успешно!");
                } else {
                    Message.setText("Ошибка во время загрузки файла, проверьте путь и попробуйте снова!");
                }
            } catch (SAXException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (path.endsWith(".json")) {
            Message.setText("Json еще не поддерживается :(");
        } else {
            Message.setText("Неправильный формат файла .. загрузите из .xml или .json");
        }
    }

    @Override
    public List<Class<? extends Shape>> getSupportedShapes() {
        return null;
    }

    @Override
    public void installPluginShape(String jarPath) {
        if (jarPath.endsWith(".jar")) {
            Path result = null;
            try {
                result = Files.move(Paths.get(jarPath), Paths.get(ShapeFactory.pathToLibs + "/"
                        + Paths.get(jarPath).getFileName()));
            } catch (IOException e) {
                System.out.println("Exception while moving file: " + e.getMessage());
            }
            if(result != null) {
                Message.setText("Плагин загружен успешно.");
            }else{
                Message.setText("Загрузка плагина провалилась.");
            }

        } else {
            Message.setText("Неправильный формат файла .. загрузите из .jar");
        }
    }


}
