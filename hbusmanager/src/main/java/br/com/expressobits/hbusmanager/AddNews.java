package br.com.expressobits.hbusmanager;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.push.CountryFromFirebase;
import br.com.expressobits.hbus.push.SendNewsToFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * @author Rafael Correa
 * @since 13/12/16
 */
public class AddNews extends JFrame {

    private String country = "BR/RS";
    private ArrayList<String> imagesUrls = new ArrayList<>();
    private News news;

    private JPanel panelMain;
    private JTextField textFieldTitle;
    private JTextField textFieldSubtitle;
    private JTextField textFieldSource;
    private JButton buttonSave;
    private JButton buttonClear;
    private JCheckBox checkBoxActived;
    private JLabel JLabelTime;
    private JTextArea textAreaBody;
    private JCheckBox checkBoxCity;
    private JSpinner spinnerCity;
    private JList listImages;
    private JButton buttonAddImageUrl;
    private JButton removeImageUrlButton;


    public AddNews() {
        this(null);
    }

    public AddNews(News news) {
        this(null, null, news);

    }

    /**
     * Construtor que abre edição de notícia da cidade.
     *
     * @param city
     * @param country
     * @param news
     */
    public AddNews(String country, City city, News news) {
        this.news = news;
        if (this.news != null) {
            textFieldTitle.setText(news.getTitle());
            textFieldSubtitle.setText(news.getSubtitle());
            textFieldSource.setText(news.getSource());
            textAreaBody.setText(news.getBody());
            checkBoxActived.setSelected(news.isActived());
        } else {

            news = new News();
            news.setTime(System.currentTimeMillis());
        }

        if (city != null && country != null) {
            checkBoxCity.setSelected(true);
            loadCities(country, city);
        }

        this.setTitle("Add News");
        this.setSize(800, 600);
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        //this.pack();
        this.setVisible(true);


        JLabelTime.setText(String.valueOf(news.getTime()));
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveNews();
            }
        });
        checkBoxCity.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (checkBoxCity.isSelected()) {
                    //loadCities();
                }

                spinnerCity.setEnabled(checkBoxCity.isSelected());
            }
        });
        buttonAddImageUrl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                imagesUrls.add(JOptionPane.showInputDialog(AddNews.this, "Add url image:", "Url image", JOptionPane.INFORMATION_MESSAGE));
                listImages.setListData(imagesUrls.toArray());
            }
        });
        removeImageUrlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!listImages.isSelectionEmpty()) {
                    imagesUrls.remove(listImages.getSelectedIndex());
                    listImages.setListData(imagesUrls.toArray());
                } else {
                    JOptionPane.showMessageDialog(AddNews.this, "Not selected image url!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    private void saveNews() {
        News news = new News();
        news.setTime(Long.parseLong(JLabelTime.getText()));
        news.setTitle(textFieldTitle.getText());
        news.setSubtitle(textFieldSubtitle.getText());
        news.setActived(checkBoxActived.isSelected());
        news.setBody(textAreaBody.getText());
        news.setSource(textFieldSource.getText());
        news.setImagesUrls(imagesUrls);
        if (checkBoxCity.isSelected()) {
            //City news
            SendNewsToFirebase.sendToFirebase((City) spinnerCity.getValue(), news);

        } else {
            //General news
            SendNewsToFirebase.sendToFirebase(news);
        }
    }

    private void loadCities(String country, City city) {
        ArrayList<City> cities = new ArrayList<>();
        CountryFromFirebase.getDatabaseReference(country).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    City city = dataSnapshot1.getValue(City.class);
                    cities.add(city);
                    System.out.println(city.getName());
                }
                updateSpinner(city, cities);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateSpinner(City city, ArrayList<City> cities) {
        SpinnerListModel citiesModel = new SpinnerListModel(cities);
        spinnerCity.setModel(citiesModel);
        //spinnerCity.s

    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(9, 4, new Insets(15, 15, 15, 15), -1, -1));
        panelMain.setEnabled(true);
        textFieldTitle = new JTextField();
        panelMain.add(textFieldTitle, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textFieldSubtitle = new JTextField();
        panelMain.add(textFieldSubtitle, new GridConstraints(1, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Subtitle");
        panelMain.add(label1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Title");
        panelMain.add(label2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldSource = new JTextField();
        panelMain.add(textFieldSource, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Source");
        panelMain.add(label3, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonClear = new JButton();
        buttonClear.setText("Clear");
        panelMain.add(buttonClear, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSave = new JButton();
        buttonSave.setText("Save");
        panelMain.add(buttonSave, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Body");
        panelMain.add(label4, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textAreaBody = new JTextArea();
        panelMain.add(textAreaBody, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        checkBoxCity = new JCheckBox();
        checkBoxCity.setText("is new local city?");
        panelMain.add(checkBoxCity, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        JLabelTime = new JLabel();
        JLabelTime.setText("time");
        panelMain.add(JLabelTime, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxActived = new JCheckBox();
        checkBoxActived.setText("Actived");
        panelMain.add(checkBoxActived, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerCity = new JSpinner();
        spinnerCity.setEnabled(false);
        panelMain.add(spinnerCity, new GridConstraints(7, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Images");
        panelMain.add(label5, new GridConstraints(4, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listImages = new JList();
        panelMain.add(listImages, new GridConstraints(4, 2, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        buttonAddImageUrl = new JButton();
        buttonAddImageUrl.setText("Add image url");
        panelMain.add(buttonAddImageUrl, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeImageUrlButton = new JButton();
        removeImageUrlButton.setText("Remove image url");
        panelMain.add(removeImageUrlButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
