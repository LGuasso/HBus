package br.com.expressobits.hbusmanager;

import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.push.CountryFromFirebase;
import br.com.expressobits.hbus.push.SendNewsToFirebase;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import com.google.firebase.database.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Rafael Correa
 * @since 14/12/16
 */
public class NewsManager extends JFrame {

    private String country = "BR/RS";

    private JList listNews;
    private JPanel panel1;
    private JButton buttonRemove;
    private JButton editButton;
    private JButton buttonAdd;
    private JComboBox comboBoxType;
    private JComboBox comboBoxCountry;
    private JComboBox comboBoxCity;
    private ArrayList<City> cities;
    private ArrayList<String> countries;
    private ArrayList<String> types;
    private ArrayList<News> newsList;
    private DefaultListModel<News> listNewsModel;

    public NewsManager() {
        this.setTitle("News Manager");
        this.setSize(800, 600);
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddNews addNews = new AddNews();
            }
        });
        loadTypes();
        comboBoxType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (types.get(comboBoxType.getSelectedIndex()).equals(FirebaseUtils.CITY_TABLE)) {
                    comboBoxCountry.setEnabled(true);
                    comboBoxCity.setEnabled(true);
                    loadCountry();
                } else {
                    comboBoxCountry.setEnabled(false);
                    comboBoxCity.setEnabled(false);
                    loadNews(types.get(comboBoxType.getSelectedIndex()));
                }
            }
        });
        comboBoxCountry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadCities(countries.get(comboBoxCountry.getSelectedIndex()));
            }
        });
        comboBoxCity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadNews(FirebaseUtils.CITY_TABLE);

            }
        });

        listNewsModel = new DefaultListModel<>();
        listNews.addFocusListener(new FocusAdapter() {
        });
        listNews.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                buttonRemove.setEnabled(true);
                editButton.setEnabled(true);
            }
        });
        buttonRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int opcao = JOptionPane.showConfirmDialog(null, "Confirme a remoção da notícia?",
                        "Remover?", JOptionPane.YES_NO_OPTION);

                News news = listNewsModel.getElementAt(listNews.getSelectedIndex());
                System.out.println(news.getTime() + " " + news.getTitle());
                if (opcao == JOptionPane.NO_OPTION) {
                    System.out.println("NO_OPTION");
                } else if (opcao == JOptionPane.YES_OPTION) {
                    String type = types.get(comboBoxType.getSelectedIndex());
                    if (type.equals(FirebaseUtils.GENERAL)) {
                        SendNewsToFirebase.getDatabaseReference().child(String.valueOf(news.getTime())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                updateRemoveJListNews(news);
                            }
                        });
                    } else {
                        SendNewsToFirebase.getDatabaseReference(cities.get(comboBoxCity.getSelectedIndex())).child(String.valueOf(news.getTime())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                updateRemoveJListNews(news);
                            }
                        });
                    }
                    System.out.println("YES_OPTION");

                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                News news = listNewsModel.getElementAt(listNews.getSelectedIndex());
                String type = types.get(comboBoxType.getSelectedIndex());
                if (type.equals(FirebaseUtils.GENERAL)) {
                    AddNews addNews = new AddNews(news);
                } else {
                    AddNews addNews = new AddNews(countries.get(comboBoxCountry.getSelectedIndex()), cities.get(comboBoxCity.getSelectedIndex()), news);
                }

            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void loadCities(String country) {
        cities = new ArrayList<>();
        CountryFromFirebase.getDatabaseReference(country).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    City city = dataSnapshot1.getValue(City.class);
                    cities.add(city);
                    System.out.println(city.getName());
                }
                updateComboBoxCity(cities);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadCountry() {
        countries = new ArrayList<>();
        countries.add(country);
        updateComboBoxCountry(countries);
    }

    private void loadTypes() {
        types = new ArrayList<>();
        types.add(FirebaseUtils.GENERAL);
        types.add(FirebaseUtils.CITY_TABLE);
        updateComboBoxTypes(types);
    }

    private void loadNews(String type) {

        listNewsModel.clear();
        DatabaseReference ref;
        if (type.equals(FirebaseUtils.GENERAL)) {
            ref = SendNewsToFirebase.getDatabaseReference();
        } else {
            ref = SendNewsToFirebase.getDatabaseReference(cities.get(comboBoxCity.getSelectedIndex()));
        }
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                News news = dataSnapshot.getValue(News.class);
                updateJListNews(news);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void updateComboBoxCity(ArrayList<City> cities) {
        DefaultComboBoxModel citiesModel = new DefaultComboBoxModel(cities.toArray());
        comboBoxCity.setModel(citiesModel);
    }

    private void updateComboBoxCountry(ArrayList<String> countries) {
        DefaultComboBoxModel countriesModel = new DefaultComboBoxModel(countries.toArray());
        comboBoxCountry.setModel(countriesModel);
    }

    private void updateComboBoxTypes(ArrayList<String> types) {
        DefaultComboBoxModel typesModel = new DefaultComboBoxModel(types.toArray());
        comboBoxType.setModel(typesModel);
    }

    private void updateJListNews(News news) {
        listNewsModel.addElement(news);
        listNews.setModel(listNewsModel);
    }

    private void updateRemoveJListNews(News news) {
        listNewsModel.removeElement(news);
        listNews.setModel(listNewsModel);
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
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        listNews = new JList();
        panel1.add(listNews, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        buttonRemove = new JButton();
        buttonRemove.setEnabled(false);
        buttonRemove.setText("Remove");
        panel1.add(buttonRemove, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonAdd = new JButton();
        buttonAdd.setText("Add");
        panel1.add(buttonAdd, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editButton = new JButton();
        editButton.setEnabled(false);
        editButton.setText("Edit");
        panel1.add(editButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxType = new JComboBox();
        panel1.add(comboBoxType, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxCountry = new JComboBox();
        comboBoxCountry.setEnabled(false);
        panel1.add(comboBoxCountry, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxCity = new JComboBox();
        comboBoxCity.setEnabled(false);
        panel1.add(comboBoxCity, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
