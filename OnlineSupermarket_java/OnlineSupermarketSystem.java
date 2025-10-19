import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

// 商品类
class Product {
    String name;
    String category;
    int stock;
    String productionDate;
    String expirationDate;
    double price;

    public Product(String name, String category, int stock, String productionDate, String expirationDate, double price) {
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Category: " + category + ", Stock: " + stock +
                ", Production Date: " + productionDate + ", Expiration Date: " + expirationDate +
                ", Price: " + price;
    }
}

// 用户类
class User {
    String username;
    String password;
    boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}

// 订单类
class Order {
    User user;
    List<Product> products;
    boolean isCompleted;

    public Order(User user, List<Product> products) {
        this.user = user;
        this.products = products;
        this.isCompleted = false;
    }
}

// 线上超市购物系统类
public class OnlineSupermarketSystem extends JFrame {
    private static final String USERS_FILE = "users.txt";
    private static final String PRODUCTS_FILE = "products.txt";
    private static final String ORDERS_FILE = "orders.txt";

    private List<User> users;
    private List<Product> products;
    private List<Order> orders;
    private User currentUser;
    private List<Product> shoppingCart;

    public OnlineSupermarketSystem() {
        users = loadUsers();
        products = loadProducts();
        orders = loadOrders();
        shoppingCart = new ArrayList<>();

        setTitle("Online Supermarket System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLoginPanel();
    }

    //登录页面
    private void showLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            for (User user : users) {
                if (user.username.equals(username) && user.password.equals(password)) {
                    currentUser = user;
                    if (user.isAdmin) {
                        showAdminPanel();
                    } else {
                        showUserPanel();
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        });

        registerButton.addActionListener(e -> showRegisterPanel());

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    //注册页面
    private void showRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel isAdminLabel = new JLabel("Is Admin (true/false):");
        JTextField isAdminField = new JTextField();
        JButton registerButton = new JButton("Register");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(isAdminLabel);
        panel.add(isAdminField);
        panel.add(registerButton);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean isAdmin = Boolean.parseBoolean(isAdminField.getText());
            User newUser = new User(username, password, isAdmin);
            users.add(newUser);
            saveUsers();
            JOptionPane.showMessageDialog(this, "Registration successful");
            showLoginPanel();
        });

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    //管理员界面
    private void showAdminPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton addProductButton = new JButton("Add Product");
        JButton deleteProductButton = new JButton("Delete Product");
        JButton modifyProductButton = new JButton("Modify Product");
        JButton findProductButton = new JButton("Find Product");
        JButton manageOrdersButton = new JButton("Manage Orders");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addProductButton);
        buttonPanel.add(deleteProductButton);
        buttonPanel.add(modifyProductButton);
        buttonPanel.add(findProductButton);
        buttonPanel.add(manageOrdersButton);
        buttonPanel.add(logoutButton);

        JTextArea productListArea = new JTextArea();
        for (Product product : products) {
            productListArea.append(product.toString() + "\n");
        }
        JScrollPane scrollPane = new JScrollPane(productListArea);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        addProductButton.addActionListener(e -> showAddProductPanel());
        deleteProductButton.addActionListener(e -> showDeleteProductPanel());
        modifyProductButton.addActionListener(e -> showModifyProductPanel());
        findProductButton.addActionListener(e -> showFindProductPanel());
        manageOrdersButton.addActionListener(e -> showManageOrdersPanel());
        logoutButton.addActionListener(e -> {
            currentUser = null;
            showLoginPanel();
        });

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    //添加商品界面
    private void showAddProductPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField();
        JLabel stockLabel = new JLabel("Stock:");
        JTextField stockField = new JTextField();
        JLabel productionDateLabel = new JLabel("Production Date:");
        JTextField productionDateField = new JTextField();
        JLabel expirationDateLabel = new JLabel("Expiration Date:");
        JTextField expirationDateField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        JButton addButton = new JButton("Add");
        JButton backButton = new JButton("Back");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(stockLabel);
        panel.add(stockField);
        panel.add(productionDateLabel);
        panel.add(productionDateField);
        panel.add(expirationDateLabel);
        panel.add(expirationDateField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(addButton);
        panel.add(backButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String category = categoryField.getText();
            int stock = Integer.parseInt(stockField.getText());
            String productionDate = productionDateField.getText();
            String expirationDate = expirationDateField.getText();
            double price = Double.parseDouble(priceField.getText());
            Product newProduct = new Product(name, category, stock, productionDate, expirationDate, price);
            products.add(newProduct);
            saveProducts();
            JOptionPane.showMessageDialog(this, "Product added successfully");
            showAdminPanel();
        });
        backButton.addActionListener(e -> {
            showAdminPanel();
        });

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    //删除商品界面
    private void showDeleteProductPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(new JLabel());
        panel.add(deleteButton);
        panel.add(backButton);

        deleteButton.addActionListener(e -> {
            String name = nameField.getText();
            for (Product product : products) {
                if (product.name.equals(name)) {
                    products.remove(product);
                    saveProducts();
                    JOptionPane.showMessageDialog(this, "Product deleted successfully");
                    showAdminPanel();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Product not found");
        });
        backButton.addActionListener(e -> {
            showAdminPanel();
        });
        backButton.addActionListener(e -> {
            showAdminPanel();
        });

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    //修改商品界面
    private void showModifyProductPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();
        JLabel categoryLabel = new JLabel("New Category:");
        JTextField categoryField = new JTextField();
        JLabel stockLabel = new JLabel("New Stock:");
        JTextField stockField = new JTextField();
        JLabel productionDateLabel = new JLabel("New Production Date:");
        JTextField productionDateField = new JTextField();
        JLabel expirationDateLabel = new JLabel("New Expiration Date:");
        JTextField expirationDateField = new JTextField();
        JLabel priceLabel = new JLabel("New Price:");
        JTextField priceField = new JTextField();
        JButton modifyButton = new JButton("Modify");
        JButton backButton = new JButton("Back");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(stockLabel);
        panel.add(stockField);
        panel.add(productionDateLabel);
        panel.add(productionDateField);
        panel.add(expirationDateLabel);
        panel.add(expirationDateField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(modifyButton);
        panel.add(backButton);

        modifyButton.addActionListener(e -> {
            String name = nameField.getText();
            for (Product product : products) {
                if (product.name.equals(name)) {
                    product.category = categoryField.getText();
                    product.stock = Integer.parseInt(stockField.getText());
                    product.productionDate = productionDateField.getText();
                    product.expirationDate = expirationDateField.getText();
                    product.price = Double.parseDouble(priceField.getText());
                    saveProducts();
                    JOptionPane.showMessageDialog(this, "Product modified successfully");
                    showAdminPanel();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Product not found");
        });
        backButton.addActionListener(e -> {
            showAdminPanel();
        });

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    //寻找商品界面
    private void showFindProductPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();
        JButton findButton = new JButton("Find");
        JButton backButton = new JButton("Back");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(new JLabel());
        panel.add(findButton);
        panel.add(backButton);

        findButton.addActionListener(e -> {
            String name = nameField.getText();
            for (Product product : products) {
                if (product.name.equals(name)) {
                    JOptionPane.showMessageDialog(this, product.toString());
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Product not found");
        });
        backButton.addActionListener(e -> {
            showAdminPanel();
        });

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    //管理订单界面
    private void showManageOrdersPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JTextArea orderListArea = new JTextArea();
    
    for (Order order : orders) {
        orderListArea.append("User: " + order.user.username + "\n");
        for (Product product : order.products) {
            orderListArea.append("  " + product.toString() + "\n");
        }
        orderListArea.append("Completed: " + order.isCompleted + "\n\n");
    }
    
    JScrollPane scrollPane = new JScrollPane(orderListArea);
    
    JButton markCompletedButton = new JButton("Mark Order as Completed");
    JLabel orderUsernameLabel = new JLabel("Order Username:");
    JTextField orderUsernameField = new JTextField(15);
    JLabel productSearchLabel = new JLabel("Product Name:");
    JTextField productSearchField = new JTextField(15);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(orderUsernameLabel);
    buttonPanel.add(orderUsernameField);
    buttonPanel.add(productSearchLabel);
    buttonPanel.add(productSearchField);
    buttonPanel.add(markCompletedButton);

    // 添加返回按钮
    JButton backButton = new JButton("Back");
    buttonPanel.add(backButton);

    // 添加搜索按钮
    JButton searchButton = new JButton("Search Orders");
    buttonPanel.add(searchButton);

    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    markCompletedButton.addActionListener(e -> {
        String username = orderUsernameField.getText();
        boolean orderFound = false;
        for (Order order : orders) {
            if (order.user.username.equals(username)) {
                order.isCompleted = true;
                saveOrders();
                orderFound = true;
                break;
            }
        }
        if (orderFound) {
            JOptionPane.showMessageDialog(this, "Order marked as completed");
            showManageOrdersPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Order not found");
        }
    });

    searchButton.addActionListener(e -> {
        String searchUsername = orderUsernameField.getText();
        String searchProductName = productSearchField.getText();
        boolean found = false;
        orderListArea.setText(""); // 清空文本区域
        for (Order order : orders) {
            if (order.user.username.equals(searchUsername)) {
                for (Product product : order.products) {
                    if (product.name.equals(searchProductName)) {
                        orderListArea.append("User: " + order.user.username + "\n");
                        orderListArea.append("  " + product.toString() + "\n");
                        orderListArea.append("Completed: " + order.isCompleted + "\n\n");
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            orderListArea.append("No orders found for user: " + searchUsername + " with product: " + searchProductName + "\n");
        }
    });

    backButton.addActionListener(e -> {
        showAdminPanel();
    });

    getContentPane().removeAll();
    getContentPane().add(panel);
    revalidate();
    repaint();
}

    //用户界面
    private void showUserPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton addToCartButton = new JButton("Add to Cart");
        JButton removeFromCartButton = new JButton("Remove from Cart");
        JButton viewCartButton = new JButton("View Cart");
        JButton checkoutButton = new JButton("Checkout");
        JButton directBuyButton = new JButton("Direct Buy");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addToCartButton);
        buttonPanel.add(removeFromCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(directBuyButton);
        buttonPanel.add(logoutButton);

        JTextArea productListArea = new JTextArea();
        for (Product product : products) {
            productListArea.append(product.toString() + "\n");
        }
        JScrollPane scrollPane = new JScrollPane(productListArea);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        addToCartButton.addActionListener(e -> showAddToCartPanel());
        removeFromCartButton.addActionListener(e -> showRemoveFromCartPanel());
        viewCartButton.addActionListener(e -> showViewCartPanel());
        checkoutButton.addActionListener(e -> checkout());
        directBuyButton.addActionListener(e -> showDirectBuyPanel());
        logoutButton.addActionListener(e -> {
            currentUser = null;
            showLoginPanel();
        });
        

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    //添加商品到购物车界面
    private void showAddToCartPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2)); // 修改布局以适应新的组件

    JLabel nameLabel = new JLabel("Product Name:");
    JTextField nameField = new JTextField();
    JLabel quantityLabel = new JLabel("Quantity:");
    JTextField quantityField = new JTextField(); // 添加数量输入字段
    JButton addButton = new JButton("Add");
    JButton backButton = new JButton("Back");

    panel.add(nameLabel);
    panel.add(nameField);
    panel.add(quantityLabel);
    panel.add(quantityField); // 添加数量输入字段到面板
    panel.add(addButton);
    panel.add(backButton);

    addButton.addActionListener(e -> {
        String name = nameField.getText();
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityField.getText()); // 获取用户输入的数量
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
            return;
        }

        for (Product product : products) {
            if (product.name.equals(name)) {
                if (product.stock >= quantity) { // 检查库存是否足够
                    for (int i = 0; i < quantity; i++) {
                        shoppingCart.add(product); // 将指定数量的商品添加到购物车
                    }
                    product.stock -= quantity; // 更新库存
                    saveProducts();
                    JOptionPane.showMessageDialog(this, "Product added to cart");
                    showUserPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough stock");
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Product not found");
    });

    backButton.addActionListener(e -> {
        showUserPanel();
    });

    getContentPane().removeAll();
    getContentPane().add(panel);
    revalidate();
    repaint();
}

    //把商品从购物车中移除界面
    private void showRemoveFromCartPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2)); // 修改布局以适应新的组件

    JLabel nameLabel = new JLabel("Product Name:");
    JTextField nameField = new JTextField();
    JLabel quantityLabel = new JLabel("Quantity:");
    JTextField quantityField = new JTextField(); // 添加数量输入字段
    JButton removeButton = new JButton("Remove");
    JButton backButton = new JButton("Back");

    panel.add(nameLabel);
    panel.add(nameField);
    panel.add(quantityLabel);
    panel.add(quantityField); // 添加数量输入字段到面板
    panel.add(removeButton);
    panel.add(backButton);

    removeButton.addActionListener(e -> {
        String name = nameField.getText();
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityField.getText()); // 获取用户输入的数量
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
            return;
        }

        int removedCount = 0;
        for (int i = 0; i < quantity; i++) {
            for (Product product : shoppingCart) {
                if (product.name.equals(name)) {
                    shoppingCart.remove(product);
                    for (Product p : products) {
                        if (p.name.equals(name)) {
                            p.stock++;
                            saveProducts();
                            removedCount++;
                            break;
                        }
                    }
                    break;
                }
            }
        }

        if (removedCount > 0) {
            JOptionPane.showMessageDialog(this, "Product removed from cart");
            showUserPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Product not found in cart or insufficient quantity");
        }
    });

    backButton.addActionListener(e -> {
        showUserPanel();
    });

    getContentPane().removeAll();
    getContentPane().add(panel);
    revalidate();
    repaint();
}

    //查看购物车界面
    private void showViewCartPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JTextArea cartListArea = new JTextArea();
    cartListArea.setEditable(false); // 设置文本区域为不可编辑

    // 统计每个商品的数量
    Map<Product, Integer> cartItems = new LinkedHashMap<>();
    for (Product product : shoppingCart) {
        cartItems.put(product, cartItems.getOrDefault(product, 0) + 1);
    }

    // 显示购物车中的商品和数量
    for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
        Product product = entry.getKey();
        int quantity = entry.getValue();
        cartListArea.append(product.toString() + " - Quantity: " + quantity + "\n");
    }

    JScrollPane scrollPane = new JScrollPane(cartListArea);
    JButton backButton = new JButton("Back");

    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(backButton, BorderLayout.SOUTH);

    backButton.addActionListener(e -> showUserPanel());

    getContentPane().removeAll();
    getContentPane().add(panel);
    revalidate();
    repaint();
}

    //付款界面
    private void checkout() {
    // 检查购物车是否为空
    if (shoppingCart.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Your cart is empty");
        return;
    }

    // 计算总金额
    double totalAmount = calculateTotalAmount();

    // 创建结账面板
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    // 显示付款信息
    JTextArea paymentInfoArea = new JTextArea();
    paymentInfoArea.setEditable(false);
    paymentInfoArea.append("Total Amount to Pay: $" + totalAmount + "\n");
    paymentInfoArea.append("Scan the QR code to pay:\n");
    paymentInfoArea.append("https:\\erweima\n"); // 模拟的二维码链接

    JScrollPane scrollPane = new JScrollPane(paymentInfoArea);
    JButton payButton = new JButton("Pay");

    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(payButton, BorderLayout.SOUTH);

    // 处理支付按钮点击事件
    payButton.addActionListener(e -> {
        // 创建新订单
        Order newOrder = new Order(currentUser, new ArrayList<>(shoppingCart));
        orders.add(newOrder);

        // 保存订单
        saveOrders();

        // 清空购物车
        shoppingCart.clear();

        // 显示结账成功提示
        JOptionPane.showMessageDialog(this, "Checkout successful");

        // 返回用户面板
        showUserPanel();
    });

    // 显示结账面板
    getContentPane().removeAll();
    getContentPane().add(panel);
    revalidate();
    repaint();
}

    //计算商品总价
    private double calculateTotalAmount() {
    double total = 0;
    for (Product product : shoppingCart) {
        total += product.price;
    }
    return total;
    }

    //直接购买界面
    private void showDirectBuyPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2)); // 修改布局以适应新的组件

    JLabel nameLabel = new JLabel("Product Name:");
    JTextField nameField = new JTextField();
    JLabel quantityLabel = new JLabel("Quantity:");
    JTextField quantityField = new JTextField(); // 添加数量输入字段
    JButton buyButton = new JButton("Buy");
    JButton backButton = new JButton("Back");

    panel.add(nameLabel);
    panel.add(nameField);
    panel.add(quantityLabel);
    panel.add(quantityField); // 添加数量输入字段到面板
    panel.add(buyButton);
    panel.add(backButton);

    buyButton.addActionListener(e -> {
        String name = nameField.getText();
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityField.getText()); // 获取用户输入的数量
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
            return;
        }

        for (Product product : products) {
            if (product.name.equals(name)) {
                if (product.stock >= quantity) { // 检查库存是否足够
                    List<Product> orderProducts = new ArrayList<>();
                    for (int i = 0; i < quantity; i++) {
                        orderProducts.add(product); // 将指定数量的商品添加到订单
                    }
                    Order newOrder = new Order(currentUser, orderProducts);
                    orders.add(newOrder);
                    product.stock -= quantity; // 更新库存
                    saveProducts();
                    saveOrders();

                    // 显示支付界面
                    showPaymentPanel(product.price * quantity);

                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough stock");
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Product not found");
    });

    backButton.addActionListener(e -> {
        showUserPanel();
    });

    getContentPane().removeAll();
    getContentPane().add(panel);
    revalidate();
    repaint();
}

    //支付界面
    private void showPaymentPanel(double totalAmount) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea paymentInfoArea = new JTextArea();
        paymentInfoArea.setEditable(false);
        paymentInfoArea.append("Total Amount to Pay: $" + totalAmount + "\n");
        paymentInfoArea.append("Scan the QR code to pay:\n");
        paymentInfoArea.append("https://erweima\n"); // 模拟的二维码链接

        JScrollPane scrollPane = new JScrollPane(paymentInfoArea);
        JButton payButton = new JButton("Pay");

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(payButton, BorderLayout.SOUTH);

        payButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Payment successful");
            showUserPanel();
        });

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    private List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                boolean isAdmin = Boolean.parseBoolean(parts[2]);
                users.add(new User(username, password, isAdmin));
            }
        } catch (IOException e) {
            // 文件不存在或读取错误，返回空列表
        }
        return users;
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.write(user.username + "," + user.password + "," + user.isAdmin);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String category = parts[1];
                int stock = Integer.parseInt(parts[2]);
                String productionDate = parts[3];
                String expirationDate = parts[4];
                double price = Double.parseDouble(parts[5]);
                products.add(new Product(name, category, stock, productionDate, expirationDate, price));
            }
        } catch (IOException e) {
            // 初始化一些商品信息
            products.add(new Product("Apple", "Fruit", 100, "2025-01-01", "2025-02-01", 2.5));
            products.add(new Product("Banana", "Fruit", 80, "2025-01-05", "2025-02-05", 1.5));
            products.add(new Product("Milk", "Dairy", 50, "2025-01-10", "2025-02-10", 3.0));
            products.add(new Product("Bread", "Bakery", 60, "2025-01-15", "2025-01-20", 2.0));
            products.add(new Product("Eggs", "Dairy", 120, "2025-01-20", "2025-02-20", 4.0));
            products.add(new Product("Chicken", "Meat", 40, "2025-01-25", "2025-02-25", 8.0));
            products.add(new Product("Beef", "Meat", 30, "2025-01-30", "2025-02-30", 12.0));
            products.add(new Product("Tomato", "Vegetable", 90, "2025-02-01", "2025-02-15", 2.0));
            products.add(new Product("Potato", "Vegetable", 110, "2025-02-05", "2025-02-20", 1.5));
            products.add(new Product("Coke", "Beverage", 70, "2025-02-10", "2025-03-10", 3.0));
            products.add(new Product("Juice", "Beverage", 60, "2025-02-15", "2025-03-15", 4.0));
            products.add(new Product("Cheese", "Dairy", 30, "2025-02-20", "2025-03-20", 5.0));
            products.add(new Product("Yogurt", "Dairy", 40, "2025-02-25", "2025-03-25", 3.5));
            products.add(new Product("Rice", "Grain", 80, "2025-02-28", "2025-04-28", 5.0));
            products.add(new Product("Pasta", "Grain", 70, "2025-03-01", "2025-04-01", 4.0));
            products.add(new Product("Butter", "Dairy", 20, "2025-03-05", "2025-04-05", 6.0));
            products.add(new Product("Jam", "Condiment", 50, "2025-03-10", "2025-05-10", 3.0));
            products.add(new Product("Mayonnaise", "Condiment", 40, "2025-03-15", "2025-05-15", 3.5));
            products.add(new Product("Ketchup", "Condiment", 60, "2025-03-20", "2025-05-20", 3.0));
            products.add(new Product("Salt", "Condiment", 100, "2025-03-25", "2025-06-25", 1.0));
            saveProducts();
        }
        return products;
    }

    private void saveProducts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE))) {
            for (Product product : products) {
                writer.write(product.name + "," + product.category + "," + product.stock + "," +
                        product.productionDate + "," + product.expirationDate + "," + product.price);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Order> loadOrders() {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String username = parts[0];
                User user = null;
                for (User u : users) {
                    if (u.username.equals(username)) {
                        user = u;
                        break;
                    }
                }
                List<Product> orderProducts = new ArrayList<>();
                String[] productNames = parts[1].split(",");
                for (String productName : productNames) {
                    for (Product product : products) {
                        if (product.name.equals(productName)) {
                            orderProducts.add(product);
                            break;
                        }
                    }
                }
                boolean isCompleted = Boolean.parseBoolean(parts[2]);
                orders.add(new Order(user, orderProducts));
                orders.get(orders.size() - 1).isCompleted = isCompleted;
            }
        } catch (IOException e) {
            // 文件不存在或读取错误，返回空列表
        }
        return orders;
    }

    private void saveOrders() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (Order order : orders) {
                StringBuilder productNames = new StringBuilder();
                for (Product product : order.products) {
                    if (productNames.length() > 0) {
                        productNames.append(",");
                    }
                    productNames.append(product.name);
                }
                writer.write(order.user.username + ";" + productNames + ";" + order.isCompleted);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OnlineSupermarketSystem system = new OnlineSupermarketSystem();
            system.setVisible(true);
        });
    }
}