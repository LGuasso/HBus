package br.com.expressobits.hbusmanager;

import br.com.expressobits.hbus.dao.SendDataToSQL;
import br.com.expressobits.hbus.files.ReadFile;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Company;
import br.com.expressobits.hbus.model.Itinerary;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador do programa no firebase.
 *
 * @author Rafael Correa
 * @since 31/12/16
 */
public class HBusManager extends JFrame {
    private JButton buttonSend;
    private JPanel panel1;
    private JComboBox comboBoxCountries;
    private JComboBox comboBoxCities;
    private JButton buttonRead;
    private JComboBox comboBoxCompanies;
    private JTable jTableItineraries;
    private JTable jTableCodes;
    private JCheckBox checkBoxItinerary;
    private JCheckBox checkBoxCode;
    private JCheckBox checkBoxBus;
    private JCheckBox checkBoxCompany;
    private JButton buttonRemoveCompanies;
    private JButton buttonRemoveItineraries;
    private JButton buttonRemoveCodes;
    private JButton buttonRemoveBuses;
    private JCheckBox singleItineraryCheckBox;
    ReadFile readFile = new ReadFile();
    List<City> cities;
    SendDataToSQL sendData;

    List<String> countries = new ArrayList<>();

    public HBusManager() {
        this.setTitle("HBUS Manager");
        this.setSize(800, 600);
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        loadCountries();


        comboBoxCountries.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadCities();
            }
        });
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sendData.open();
                City city = cities.get(comboBoxCities.getSelectedIndex());
                /*for (Company company : sendData.getCompanies(city)) {
                 sendData.sendCompany(city, company);
                 }*/
                sendData.sendCompany(city, sendData.getCompanies(city).get(0));
                sendData.close();

            }
        });
        comboBoxCities.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buttonRead.setEnabled(true);
            }
        });
        buttonRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                City city = cities.get(comboBoxCities.getSelectedIndex());
                sendData = new SendDataToSQL(city);
                sendData.readData(city);
                loadCompanies(city);
            }
        });
        comboBoxCompanies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadItineraryTable();
                loadCodeTable();
            }
        });
    }

    public void loadItineraryTable() {
        String[] itineraryColunas = new String[]{"Name", "N*s buses"};
        DefaultTableModel itineraryTableModel = new DefaultTableModel(itineraryColunas, 0);
        City city = cities.get(comboBoxCities.getSelectedIndex());
        Company company = sendData.getCompanies(city).get(comboBoxCompanies.getSelectedIndex());

        for (Itinerary itinerary : sendData.getItineraries(city, company)) {
            String[] campos = {itinerary.getName(), sendData.getSizeBusesOfItinerary(company, itinerary) + ""};
            itineraryTableModel.addRow(campos);
        }
        jTableItineraries.setModel(itineraryTableModel);
    }

    public void loadCodeTable() {
        String[] codeColunas = new String[]{"Code", "Description"};
        DefaultTableModel codeTableModel = new DefaultTableModel(codeColunas, 0);
        City city = cities.get(comboBoxCities.getSelectedIndex());
        Company company = sendData.getCompanies(city).get(comboBoxCompanies.getSelectedIndex());

        for (Code code : sendData.getCodes(city, company)) {
            String[] campos = {code.getName(), code.getDescrition()};
            codeTableModel.addRow(campos);
        }
        jTableCodes.setModel(codeTableModel);
    }

    public void loadCountries() {
        countries.add("BR/RS");
        DefaultComboBoxModel defaultComboBoxModelCountries = new DefaultComboBoxModel(countries.toArray());
        comboBoxCountries.setModel(defaultComboBoxModelCountries);
    }

    public void loadCities() {
        cities = readFile.getCities(countries.get(comboBoxCountries.getSelectedIndex()));
        DefaultComboBoxModel defaultComboBoxModelCities = new DefaultComboBoxModel(cities.toArray());
        comboBoxCities.setModel(defaultComboBoxModelCities);
    }

    public void loadCompanies(City city) {
        DefaultComboBoxModel defaultComboBoxModelCities = new DefaultComboBoxModel(sendData.getCompanies(city).toArray());
        comboBoxCompanies.setModel(defaultComboBoxModelCities);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
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
        panel1.setLayout(new GridLayoutManager(5, 6, new Insets(0, 0, 0, 0), -1, -1));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(405);
        panel1.add(splitPane1, new GridConstraints(3, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        splitPane1.setLeftComponent(scrollPane1);
        jTableCodes = new JTable();
        scrollPane1.setViewportView(jTableCodes);
        final JScrollPane scrollPane2 = new JScrollPane();
        splitPane1.setRightComponent(scrollPane2);
        jTableItineraries = new JTable();
        scrollPane2.setViewportView(jTableItineraries);
        comboBoxCountries = new JComboBox();
        panel1.add(comboBoxCountries, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxCompany = new JCheckBox();
        checkBoxCompany.setText("Company");
        panel1.add(checkBoxCompany, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxItinerary = new JCheckBox();
        checkBoxItinerary.setText("Itinerary");
        panel1.add(checkBoxItinerary, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxCode = new JCheckBox();
        checkBoxCode.setText("Code");
        panel1.add(checkBoxCode, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBoxBus = new JCheckBox();
        checkBoxBus.setText("Bus");
        panel1.add(checkBoxBus, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxCities = new JComboBox();
        panel1.add(comboBoxCities, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxCompanies = new JComboBox();
        panel1.add(comboBoxCompanies, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSend = new JButton();
        buttonSend.setText("Send");
        panel1.add(buttonSend, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRead = new JButton();
        buttonRead.setEnabled(false);
        buttonRead.setText("Read");
        panel1.add(buttonRead, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRemoveCompanies = new JButton();
        buttonRemoveCompanies.setText("Remove Companies");
        panel1.add(buttonRemoveCompanies, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRemoveItineraries = new JButton();
        buttonRemoveItineraries.setText("Remove Itineraries");
        panel1.add(buttonRemoveItineraries, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRemoveCodes = new JButton();
        buttonRemoveCodes.setText("Remove Codes");
        panel1.add(buttonRemoveCodes, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRemoveBuses = new JButton();
        buttonRemoveBuses.setText("Remove Bus");
        panel1.add(buttonRemoveBuses, new GridConstraints(2, 4, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        singleItineraryCheckBox = new JCheckBox();
        singleItineraryCheckBox.setText("Single Itinerary");
        panel1.add(singleItineraryCheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
