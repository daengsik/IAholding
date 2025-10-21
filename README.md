# MythicMobs holding addon
이 애드온은 MythicMobs 의 TriggerCondition: holding 이
ItemsAdder 의 커스텀아이템을 인식하게 만드는 애드온입니다.

```
TriggerCondition:
- iaholding{i=myitem:item_name} true
```

형태로 사용이 가능합니다.

## 종속성
- MythicMobs
- ItemsAdder

## 빌드 방법

### 1. 필수 jar 파일 준비
빌드하기 전에 서버에서 사용 중인 플러그인 jar 파일을 `libs` 폴더에 복사해야 합니다:

```
libs/
  ├── MythicMobs-5.x.x.jar (또는 MythicLib-x.x.x.jar)
  └── ItemsAdder-x.x.x.jar
```

**파일 위치:**
- MythicMobs: 서버의 `plugins` 폴더에서 찾을 수 있습니다
- ItemsAdder: 서버의 `plugins` 폴더에서 찾을 수 있습니다

### 2. 빌드 실행
```bash
# Windows
.\gradlew.bat build

# Linux/Mac
./gradlew build
```

### 3. 빌드된 파일 위치
빌드가 성공하면 `build/libs/` 폴더에 jar 파일이 생성됩니다.