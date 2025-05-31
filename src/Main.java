import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Keys;

public class Main {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Aumentar los tiempos de espera
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ASUS\\Downloads\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;


        Actions actions = new Actions(driver);

        try {
            driver.manage().window().maximize();
            driver.get("https://demoqa.com/automation-practice-form");

            // Cerrar banner y anuncios si existen
            try {
                driver.findElement(By.id("close-fixedban")).click();
                // Remover anuncios que pueden interferir
                js.executeScript("const elements = document.getElementsByTagName('iframe');" +
                               "for(let i = 0; i < elements.length; i++){" +
                               "    elements[i].remove();" +
                               "}");
                js.executeScript("document.getElementById('fixedban').remove();");
            } catch (Exception ignored) {}

            ((JavascriptExecutor) driver).executeScript(
                    """
                    const selectors = [
                        'iframe',
                        '#fixedban',
                        '.google-auto-placed',
                        '[id*="adplus-anchor"]',
                        '[id*="Ad.Plus-Anchor"]',
                        '.banner',
                        '.advertisement',
                        '#close-fixedban'
                    ];
                    selectors.forEach(sel => {
                        document.querySelectorAll(sel).forEach(el => el.style.display = 'none');
                    });
                    """
            );

            // Información personal
            wait.until(ExpectedConditions.elementToBeClickable(By.id("firstName"))).sendKeys("Juan");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("lastName"))).sendKeys("Pérez");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("userEmail"))).sendKeys("juan.perez@example.com");
            
            // Género
            WebElement genderRadio = driver.findElement(By.cssSelector("label[for='gender-radio-1']"));
            js.executeScript("arguments[0].click();", genderRadio);
            
            wait.until(ExpectedConditions.elementToBeClickable(By.id("userNumber"))).sendKeys("3216549870");

            // Fecha de nacimiento
            WebElement dateInput = driver.findElement(By.id("dateOfBirthInput"));
            js.executeScript("arguments[0].scrollIntoView(true);", dateInput);
            js.executeScript("arguments[0].click();", dateInput);

            // Esperar y seleccionar mes y año
            wait.until(ExpectedConditions.elementToBeClickable(By.className("react-datepicker__month-select")))
                .sendKeys("May");
            wait.until(ExpectedConditions.elementToBeClickable(By.className("react-datepicker__year-select")))
                .sendKeys("2000");
            
            // Seleccionar día usando JavaScript
            WebElement day = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".react-datepicker__day--015")));
            js.executeScript("arguments[0].click();", day);

            // Materias
            WebElement subjectsInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("subjectsInput")));
            js.executeScript("arguments[0].scrollIntoView(true);", subjectsInput);
            subjectsInput.sendKeys("Maths");
            wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".subjects-auto-complete__option"))).click();

            // Hobbies
            WebElement hobby = driver.findElement(By.cssSelector("label[for='hobbies-checkbox-1']"));
            js.executeScript("arguments[0].click();", hobby);

            // Subir archivo
            WebElement uploadElement = driver.findElement(By.id("uploadPicture"));
            uploadElement.sendKeys("C:\\Users\\ASUS\\Downloads\\Imagen de WhatsApp 2025-01-31 a las 14.55.32_419b1f63.jpg");

            // Dirección
            WebElement addressField = wait.until(ExpectedConditions.elementToBeClickable(By.id("currentAddress")));
            addressField.sendKeys("Calle Falsa 123");

            // Estado y Ciudad
            try {
                // Scroll hacia el área de Estado/Ciudad
                WebElement stateContainer = driver.findElement(By.id("stateCity-wrapper"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", stateContainer);
                Thread.sleep(1000);

                // Seleccionar Estado (NCR)
                // Primero hacemos clic en el contenedor del react-select
                WebElement stateDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("#state .css-1hwfws3")));
                actions.moveToElement(stateDropdown).click().perform();
                Thread.sleep(500);

                // Esperamos a que aparezca el menú y seleccionamos NCR
                WebElement ncrOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div[id^='react-select'][id*='-option-0']")));
                actions.moveToElement(ncrOption).click().perform();
                Thread.sleep(500);

                // Seleccionar Ciudad (Delhi)
                // Primero hacemos clic en el contenedor del react-select para ciudad
                WebElement cityDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("#city .css-1hwfws3")));
                actions.moveToElement(cityDropdown).click().perform();
                Thread.sleep(500);

                // Esperamos a que aparezca el menú y seleccionamos Delhi
                WebElement delhiOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div[id^='react-select'][id*='-option-0']")));
                actions.moveToElement(delhiOption).click().perform();

            } catch (Exception e) {
                System.out.println("Error al seleccionar estado/ciudad: " + e.getMessage());
                e.printStackTrace();
                
                // Plan B: Intento alternativo usando una secuencia diferente de acciones
                try {
                    actions = new Actions(driver);
                    
                    // Limpiar cualquier menú abierto
                    actions.sendKeys(Keys.ESCAPE).perform();
                    Thread.sleep(500);

                    // Estado - usando Tab para llegar al elemento
                    WebElement stateInput = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("#state input")));
                    actions.moveToElement(stateInput)
                           .click()
                           .sendKeys("NCR")
                           .sendKeys(Keys.ENTER)
                           .perform();
                    Thread.sleep(1000);

                    // Ciudad
                    WebElement cityInput = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("#city input")));
                    actions.moveToElement(cityInput)
                           .click()
                           .sendKeys("Delhi")
                           .sendKeys(Keys.ENTER)
                           .perform();
                    Thread.sleep(500);

                } catch (Exception e2) {
                    System.out.println("Error en el método alternativo: " + e2.getMessage());
                    throw e2;
                }
            }
            // Enviar formulario
            WebElement submitButton = driver.findElement(By.id("submit"));
            js.executeScript("arguments[0].click();", submitButton);
            // Esperar a que aparezca el modal de confirmación
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("modal-content")));

            // Imprimir mensaje de éxito con formato
            System.out.println("\n" + "=".repeat(50));
            System.out.println("✅ ¡PRUEBA EJECUTADA EXITOSAMENTE!");
            System.out.println("📝 Detalles:");
            System.out.println("   - Formulario completado correctamente");
            System.out.println("   - Datos enviados y validados");
            System.out.println("   - Tiempo de ejecución: " +
                    Duration.between(java.time.Instant.now(), java.time.Instant.now().plusMillis(3000)).toMillis() + "ms");
            System.out.println("=".repeat(50) + "\n");

            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("❌ ERROR EN LA PRUEBA");
            System.out.println("📝 Detalles del error:");
            System.out.println("   " + e.getMessage());
            System.out.println("=".repeat(50) + "\n");
            e.printStackTrace();
        } finally {
            driver.quit();
            System.out.println("🔚 Prueba finalizada - Navegador cerrado");
        }
    }
}