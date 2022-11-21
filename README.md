# HallymMap

<img alt="Project Icon" src="sw_exhibit_hm.png" width="720"/>

<img alt="Project Icon" src="/app/src/main/ic_hm_playstore_image.png" width="720"/>

> 학교 지도와 건물, 강의실 위치, 편의시설 정보를 제공하는 안드로이드 애플리케이션입니다.

<br>

## 목차
1. [설치](#설치)
2. [스크린샷](#스크린샷)
3. [설명](#설명)
4. [사용된 라이브러리](#사용된-라이브러리)
5. [개발 내용](#개발-내용)
6. [개인정보처리방침](#개인정보처리방침)

<br><br>

## 설치
<img alt="Project Icon" src="/app/src/main/ic_launcher-playstore.png" width="240" height="240"/>

**Android: [Google Play](https://play.google.com/store/apps/details?id=xyz.ridsoft.hal) (사전 체험 진행중)**

<br>`안드로이드 10 (Q)` 이상 권장
<br>`안드로이드 7 (N)` 이상 요구됨

<br><br>

## 스크린샷
### 라이트 모드
<img alt="Screenshot" src="/res/screenshots/light_01.png" width="180px"/> <img alt="Screenshot" src="/res/screenshots/light_02.png" width="180px"/> <img alt="Screenshot" src="/res/screenshots/light_03.png" width="180px"/> <img alt="Screenshot" src="/res/screenshots/light_04.png" width="180px"/>
<br>
### 다크 모드
<img alt="Screenshot" src="/res/screenshots/dark_01.png" width="180px"/> <img alt="Screenshot" src="/res/screenshots/dark_02.png" width="180px"/> <img alt="Screenshot" src="/res/screenshots/dark_03.png" width="180px"/> <img alt="Screenshot" src="/res/screenshots/dark_04.png" width="180px"/>
<br>

## 설명
한림대학교 학생과 방문자를 위한 지도 애플리케이션입니다.
<br>교내 시설 정보를 확인하고 검색할 수 있도록 도와줍니다.
<br>
* 🗺 학교 지도를 확인할 수 있습니다.
* 📍 주요 시설의 위치 정보를 제공합니다.
* 🔍 검색 기능을 통해 강의실 번호와 같이 원하는 장소를 검색해 보세요.
* 👍 즐겨 찾는 장소를 등록하여 빠르게 접근할 수 있습니다.
<br>

HallymMap은 사용자가 직접 편집하고 기여할 수 있는 오픈소스 지도 [OpenStreetMap](https://www.openstreetmap.org)을 사용합니다.
<br>
<br>

## 사용된 라이브러리
* osmdroid: https://github.com/osmdroid/osmdroid
* gson: https://github.com/google/gson
* jsoup: https://jsoup.org
* Material Icons: https://fonts.google.com/icons
<br>

## 개발 내용

### 1. 지도 업데이트
* 오픈스트리트맵의 학교 지도에 최신 정보가 적용되어 있지 않아 정보를 업데이트했습니다.
  <br><img src="/res/dev/img_01.png" width="720px"/>
#### 업데이트 전, 후 지도 비교
<br><img src="/res/dev/img_02.png" width="360px"/> <img src="/res/dev/img_03.png" width="360px"/>
<br>

---

### 2. 앱 디자인
* HallymMap은 '지도', '좋아요', '시설', '더보기'의 4가지 탭으로 이루어져 있습니다.
* 하단의 NavigationBar를 통해 MainActivity에 각 탭별 Fragment를 전환합니다.
    * 검색 등의 기능은 심화적인 기능 구현을 위해 별도의 Activity로 동작합니다.
      <br><br><img src="/res/dev/img_04-1.png" width="720px"/>
      <br><br>

#### 디자인 변화
<img src="/res/dev/img_05-1.png" width="720px"/>

* `Version 1`
    * 최초 버전
* `Version 2`
    * MainActivity의 FloatingActionButton 제거
    * MapFragment에 FloatingActionButton 추가
    * 한손 조작 편의성을 위해 Control bar를 아래로 배치
* `Version 3`
    * 검색 버튼과 내 위치 버튼의 위치를 교환
    * '좋아요'한 장소가 없을 경우 알림 메시지 출력
* `Version 4`
    * 검색 버튼 삭제 및 검색바 추가
    * SearchActivity로 전환 시 원형 애니메이션 시작 위치 변경

<br>

#### User Interface
<img src="/res/screenshots/ss_01.png" width="160px"/> <img src="/res/screenshots/ss_02.png" width="160px"/> <img src="/res/screenshots/ss_03.png" width="160px"/> <img src="/res/screenshots/ss_04.gif" width="160px"/> 

* `Screenshot 1`: 한 손으로도 접근이 쉬운 하단 `Chip`을 통해 지도에 시설별로 `핀`을 고정할 수 있습니다.
* `Screenshot 2`: `핀`을 선택하면 나타나는 `Bottom Sheet`에서 해당 장소의 정보를 한 눈에 확인할 수 있습니다.
* `Screenshot 3`: 특정 검색 결과는 알기 쉬운 방법으로 사용자에게 표시됩니다.
* `Screenshot 4`: 간단한 애니메이션으로 사용자가 한 공간에서 연속적인 작업을 하는 것 처럼 느끼게 해줍니다.

<br><br>

---

### 3. 데이터

#### 1) 지도 데이터
* 건물과 시설 데이터는 앱 내에 json으로 저장된 데이터를 기본으로 사용합니다.
```json
{
    "floor": 1,
    "type": "STUDY_ROOM",
    "buildingNo": 1,
    "en": "SW Village",
    "id": 30519,
    "kr": "소프트웨어 빌리지",
    "latitude": 37.88643,
    "longitude": 127.73589,
    "name": "소프트웨어 빌리지",
    "searchTag": "공학관,소프트웨어,software,sw"
  }
```
* 그러나 지도 데이터가 업데이트 될 때마다 앱을 업데이트하는 것은 비효율적이므로, SpreadSheet를 이용하여 최신 정보를 유지합니다.
* 셀룰러 데이터 사용과 앱의 부하를 최소화하기 위해 앱 접속시 매번 업데이트를 진행하지 않습니다. 따라서 최신 데이터가 표시되기까지 최대 이틀이 걸릴 수도 있습니다.
* SpreadSheet의 데이터는 [Jsoup](https://jsoup.org)으로 파싱합니다.
```kotlin
const val URL = "..." // SpreadSheet web URL

// Jsoup으로 파싱
Jsoup.connect(URL).get()
```
#### 2) 데이터의 저장
* HallymMap은 안드로이드에서 제공하는 Key-Value 형식의 SharedPreference에 모든 데이터를 저장합니다.
    * 앱 설정 데이터
    * 지도 업데이트 데이터
    * 개발자 모드 데이터
    * '좋아요'한 장소 데이터
    * ~~사용자 데이터~~ *제거됨*
```text
(예시)
"app" preference
  ┗ "initialized": true
  ┗ "updated": true
  ┗ ...

"map" preference
  ┗ "place_keys": "{48253, 82190, 67963, ...}"
  ┗ "48253": "{"id": 48253, "name": "SW Village", "latitude": 37.88643, "longitude": 127.73589}"
  ┗ "82189": "{"id": 82189, "name": "학생식당", "latitude": 37.8868767, "longitude": 127.7400138}"
  ┗ ...
```

<br><br>

## 개인정보처리방침

[개인정보처리방침](/PRIVACY.md)에서 확인하십시오.

<br>
