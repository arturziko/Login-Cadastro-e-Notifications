import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Classe que representa um usuário com nome, senha e email
class User {
    private String username;
    private String password;
    private String email;

    // Construtor que inicializa os dados do usuário
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Retorna o nome de usuário
    public String getUsername() {
        return username;
    }

    // Retorna a senha do usuário
    public String getPassword() {
        return password;
    }

    // Retorna o email do usuário
    public String getEmail() {
        return email;
    }

    // Representação em texto do usuário para exibição
    @Override
    public String toString() {
        return username + " (" + email + ")";
    }
}

// Classe que gerencia o cadastro e o login de usuários
class UserManager {
    private List<User> users = new ArrayList<>(); // lista interna de usuários

    // Cadastra um novo usuário na lista
    public void registerUser(String username, String password, String email) {
        users.add(new User(username, password, email));
    }

    // Tenta fazer login procurando usuário e senha correspondentes
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // retorno do usuário com credenciais válidas
            }
        }
        return null; // se não encontrar, retorna nulo
    }

    // Retorna a lista de usuários cadastrados
    public List<User> getUsers() {
        return users;
    }
}

// Classe de serviço para envio e recebimento de notificações
class NotificationService {
    // Exibe no console os dados da notificação enviada
    public void sendNotification(User from, User to, String message) {
        System.out.println("\n[NOTIFICAÇÃO ENVIADA]");
        System.out.println("De: " + from.getUsername());
        System.out.println("Para: " + to.getUsername());
        System.out.println("Mensagem: " + message);
    }

    // Exibe no console a notificação recebida pelo usuário
    public void receiveNotification(User to, String message) {
        System.out.println("\n[NOTIFICAÇÃO RECEBIDA]");
        System.out.println("Usuário: " + to.getUsername());
        System.out.println("Mensagem: " + message);
    }
}

// Classe principal do aplicativo
public class app {
    private static UserManager userManager = new UserManager();
    private static NotificationService notificationService = new NotificationService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedUsers(); // cria usuários iniciais para teste
        System.out.println("=== Sistema de Login, Cadastro e Notificações ===");

        while (true) {
            // Mostra o menu principal
            System.out.println("\nEscolha uma etapa:");
            System.out.println("1 - Login de usuário");
            System.out.println("2 - Cadastro de usuário");
            System.out.println("3 - Enviar notificação");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    performLogin();
                    break;
                case "2":
                    performRegistration();
                    break;
                case "3":
                    performNotification();
                    break;
                case "0":
                    System.out.println("Encerrando o sistema.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    // Adiciona alguns usuários iniciais ao sistema
    private static void seedUsers() {
        userManager.registerUser("aluno1", "1234", "aluno1@faculdade.com");
        userManager.registerUser("aluno2", "abcd", "aluno2@faculdade.com");
    }

    // Realiza o processo de login perguntando usuário e senha
    private static void performLogin() {
        System.out.println("\n=== Login de usuário ===");
        System.out.print("Nome de usuário: ");
        String username = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();

        User user = userManager.login(username, password);
        if (user != null) {
            System.out.println("Login realizado com sucesso. Bem-vindo, " + user.getUsername() + "!");
        } else {
            System.out.println("Login falhou. Verifique usuário e senha.");
        }
    }

    // Realiza o cadastro de um novo usuário no sistema
    private static void performRegistration() {
        System.out.println("\n=== Cadastro de usuário ===");
        System.out.print("Nome de usuário: ");
        String username = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        userManager.registerUser(username, password, email);
        System.out.println("Usuário cadastrado com sucesso: " + username);
    }

    // Realiza o envio de notificações de um usuário para outro
    private static void performNotification() {
        System.out.println("\n=== Sistema de Notificações ===");
        System.out.print("Digite seu nome de usuário: ");
        String senderName = scanner.nextLine();
        System.out.print("Digite sua senha: ");
        String senderPassword = scanner.nextLine();

        User sender = userManager.login(senderName, senderPassword);
        if (sender == null) {
            System.out.println("Falha no login. Não é possível enviar notificações.");
            return; // interrompe se login inválido
        }

        List<User> users = userManager.getUsers();
        if (users.size() < 2) {
            System.out.println("Não há outros usuários cadastrados para enviar notificações.");
            return; // não há destinatários disponíveis
        }

        System.out.println("Escolha o destinatário:");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (!user.getUsername().equals(sender.getUsername())) {
                System.out.println((i + 1) + " - " + user);
            }
        }

        System.out.print("Número do destinatário: ");
        String targetOption = scanner.nextLine();

        int targetIndex;
        try {
            targetIndex = Integer.parseInt(targetOption) - 1; // converte a opção em índice
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
            return;
        }

        if (targetIndex < 0 || targetIndex >= users.size()) {
            System.out.println("Destinatário inválido.");
            return;
        }

        User recipient = users.get(targetIndex);
        if (recipient.getUsername().equals(sender.getUsername())) {
            System.out.println("Você não pode enviar notificação para si mesmo.");
            return;
        }

        System.out.print("Mensagem: ");
        String message = scanner.nextLine();
        notificationService.sendNotification(sender, recipient, message);
        notificationService.receiveNotification(recipient, message);
    }
}
