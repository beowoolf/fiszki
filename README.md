# Fiszki - Aplikacja do Nauki

## 📝 Opis
Fiszki to nowoczesna aplikacja webowa stworzona w ramach szkolenia 10xDevs, umożliwiająca efektywną naukę poprzez system fiszek. Aplikacja została zbudowana przy użyciu Spring Boot i wykorzystuje najnowsze technologie w ekosystemie Java.

## 🚀 Technologie
- Java 21
- Spring Boot 3.2.4
- Spring Security
- Spring Data JPA
- Thymeleaf
- MariaDB
- Docker
- Maven
- Lombok
- TestContainers

## 🛠️ Wymagania
- JDK 21
- Maven
- Docker i Docker Compose
- MariaDB (lub użyj konteneryzacji)

## 🏗️ Instalacja i Uruchomienie

### Używając Docker Compose
1. Sklonuj repozytorium:
```bash
git clone [URL_REPOZYTORIUM]
```

2. Uruchom aplikację:
```bash
docker-compose up -d
```

### Uruchomienie lokalne
1. Sklonuj repozytorium
2. Zbuduj projekt:
```bash
./mvnw clean install
```

3. Uruchom aplikację:
```bash
./mvnw spring-boot:run
```

## 🧪 Testy
Projekt zawiera kompleksowe testy jednostkowe i integracyjne. Aby uruchomić testy:
```bash
./mvnw test
```

## 📚 Struktura Projektu
```
src/
├── main/
│   ├── java/
│   │   └── pl/mojezapiski/fiszki/
│   │       ├── config/        # Konfiguracja aplikacji
│   │       ├── controller/    # Kontrolery
│   │       ├── model/         # Modele danych
│   │       ├── repository/    # Repozytoria
│   │       └── service/       # Serwisy
│   └── resources/
│       ├── static/           # Zasoby statyczne
│       └── templates/        # Szablony Thymeleaf
└── test/                    # Testy
```

## 🔒 Bezpieczeństwo
Aplikacja wykorzystuje Spring Security do zarządzania bezpieczeństwem. Wszystkie endpointy są zabezpieczone i wymagają odpowiedniej autoryzacji.

## 🤝 Współpraca
Zachęcamy do współpracy! Jeśli chcesz przyczynić się do rozwoju projektu:
1. Zrób fork repozytorium
2. Utwórz branch dla swojej funkcjonalności
3. Zrób commit zmian
4. Utwórz pull request

## 📞 Kontakt
W przypadku pytań lub sugestii, prosimy o utworzenie issue w repozytorium projektu. 