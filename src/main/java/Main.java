import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        MyCalculator myCalculator = new MyCalculator();
        myCalculator.start();
    }
}

class MyCalculator {
    int persons;
    Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
    ArrayList<MyProduct> productsList = new ArrayList<>();
    MyGrammatika gramma = new MyGrammatika();

    public void start() {
        boolean isContinue = true;
        System.out.println("Калькулятор расчета готов к использованию!");
        System.out.print("Введите количество персон: ");

        while (true) {
            if (scanner.hasNextInt()) {
                persons = scanner.nextInt();
                scanner.nextLine();
                if (persons == 1) {
                    System.out.println("Весь счет оплачиваете Вы. Конец расчета");
                    return;
                } else if (persons < 1) {
                    System.out.println("Некорректный ввод. Попробуйте снова");
                } else {
                    System.out.printf("Счет будет разделен на %d %s%n", persons, gramma.grammaRules(persons, "персона"));
                    break;
                }
            } else {
                System.out.println("Некорректный ввод. Введите целое число");
                scanner.next();
            }
        }
        System.out.println("Добавляем товары в счет. Для завершения введите ЗАВЕРШИТЬ");
        while (isContinue) {
            System.out.print("Введите название товара: ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("Завершить") && productsList.isEmpty()) {
                System.out.println("Счет пуст. Расчет окончен");
                return;
            } else if (userInput.equalsIgnoreCase("Завершить") && !productsList.isEmpty()) {
                break;
            } else {
                System.out.print("Введите стоимость: ");
                if (scanner.hasNextDouble()) {
                    double userInputCoast = scanner.nextDouble();
                    scanner.nextLine();
                    if (userInputCoast < 0) {
                        System.out.println("Некорректный ввод. Введите товар заново.");
                    } else {
                        MyProduct product = new MyProduct(userInput, userInputCoast);
                        productsList.add(product);
                        System.out.println("*Товар успешно добавлен*");
                        System.out.println("Желаете продолжить? [Да/Нет/Завершить]");
                        while (true) {
                            userInput = scanner.nextLine();
                            if (userInput.equalsIgnoreCase("да")) {
                                break;
                            } else if (userInput.equalsIgnoreCase("нет") || userInput.equalsIgnoreCase("Завершить")) {
                                isContinue = false;
                                break;
                            } else {
                                System.out.println("Неизвестная команда. Введите Да/Нет/Завершить");
                            }

                        }
                    }
                } else {
                    System.out.println("Некорректный ввод. Введите товар заново");
                    scanner.nextLine();
                }
            }
        }
        printProductsList();
    }

    public void printProductsList() {
        Iterator<MyProduct> iterator = productsList.iterator();
        double sum = 0.00;
        System.out.println("**************");
        System.out.println("Печатаю итоговый чек: ");
        while (iterator.hasNext()) {
            MyProduct product = iterator.next();
            sum = sum + product.cost;
            System.out.println(product.name + " " + String.format(Locale.US, "%.2f", product.cost) + " " + gramma.grammaRules(product.cost, "рубль"));
        }
        System.out.println("**************");
        System.out.printf(Locale.US, "Всего позиций: %d. Общая сумма = %.2f %s.%n", productsList.size(), sum, gramma.grammaRules(sum, "рубль"));
        System.out.printf(Locale.US, "Каждый должен заплатить %.2f %s", sum / persons, gramma.grammaRules(sum / persons, "рубль"));
    }
}

class MyGrammatika {

    String grammaRules(double dig, String word) {
        double x = dig % 10;
        if (word.equals("рубль")) {
            if (dig % 100 >= 11 && dig % 100 <= 19) {
                return ("рублей");
            } else {
                return switch ((int) x) {
                    case 1 -> ("рубль");
                    case 2, 3, 4 -> ("рубля");
                    default -> ("рублей");
                };
            }
        }
        if (word.equals("персона")) {
            if (dig % 100 >= 11 && dig % 100 <= 19) {
                return ("персон");
            } else {
                return switch ((int) x) {
                    case 1 -> ("персона");
                    case 2, 3, 4 -> ("персоны");
                    default -> ("персон");
                };
            }
        }
        return ("");
    }
}

class MyProduct {
    String name;
    double cost;

    MyProduct(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }
}
