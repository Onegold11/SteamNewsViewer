# SteamNewsViewer

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/Onegold11/SteamNewsViewer">
    <img src="image/app_icon.png" alt="icon" width="160" height="160">
  </a>

  <h3 align="center">Stean News Viewer</h3>

  <p align="center">
    Steam app 뉴스를 확인하는 앱
    <br />
    <a href="https://github.com/Onegold11/SteamNewsViewer"><strong>Explore the docs »</strong></a>
    <br />
    <a href="https://github.com/Onegold11/SteamNewsViewer/issues">Report Bug</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<p align="center"><img src="image/example.png" alt="icon" width="200" height="400"></p>

Steam의 Open Api를 사용해 게임 정보와 뉴스 정보를 Retrofit2 라이브러리를 사용해 얻음.
정보는 Room을 사용해 SQLite 데이터베이스에 저장하고 Glide 라이브러리를 사용해 앱과 뉴스의 미리보기 이미지를 화면에 출력. 네트워크와 데이터베이스의 비동기 처리를 coroutine을 사용해 처리했으며 data binding을 사용해 UI를 다루고 View model과 Live data를 사용해 화면 조작을 다룸.

Here's why:
* 백엔드 데이터베이스 서버 없는 이유 : 사용자를 특정할 수 있는 데이터를 만들지 않았고 서버 구축이 아직 미숙해서 만들지 않음. 다만 기회가 되면 SSO와 OAuth, 백엔드 서버를 사용한 Steam 로그인을 구현하고 싶음
* RxKotlin을 사용하지 않은 이유 : 반응형 프로그래밍인 Rx 라이브러리의 사용법을 아직 이해하지 못해 적용하지 못했음. 마찬가지로 기회가 되면 Room과 Retrofit을 RxKotlin을 사용해 연결하고 싶음
* MVVM 구조가 제대로 이뤄지지 않은 이유 : MVVM 패턴을 처음 적용해보고, 프래그먼트와 리사이클러뷰에서 MVVM 패턴을 적용하는 방법을 아직 제대로 몰라서 MainActivity에만 부분적으로 적용했다.
* 프로젝트 관리 툴이 제대로 이뤄지지 않은 이유 : 내가 가장 후회하는 단점이다. 예상보다 오래 걸렸고 제대로 업무를 나눠놓지 않았다. 다음 프로젝트를 진행할 때 Trello나 Redmine 등을 사용해봐야 겠다.

이 프로젝트가 누군가에게 쓰일 지는 모르겠지만, 그 사람에게 도움이 됐으면 바란다.

### Built With

* [Retrofit2](https://square.github.io/retrofit/)
* [Glide](https://github.com/bumptech/glide)
* [Data binding, Room, View model, Live data](https://developer.android.com/topic/libraries/architecture?hl=ko)
* [jsoup](https://jsoup.org/download)



<!-- GETTING STARTED -->
## Getting Started

프로젝트는 fork 또는 다운로드를 사용해 복사하면 된다. 앱은 구글 플레이 스토어에 등록.


<!-- USAGE EXAMPLES -->
## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

_For more examples, please refer to the [Documentation](https://example.com)_


<!-- CONTRIBUTING -->
## Contributing

프로젝트에 미숙한 부분이 있다면 양해를 부탁하겠으며 버그 리포트, pull request 등 어떤 기여라도 환영함. 아래는 예시이며 편한 형태로 기여해주면 감사히 수정하겠음.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Onegold11 - ujini1129@gmail.com




<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements
* [Steam Web API](https://steamcommunity.com/dev)
* [Steam Web API2](https://partner.steamgames.com/doc/webapi_overview)
* [Retrofit](http://devflow.github.io/retrofit-kr/)





<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/Onegold11/SteamNewsViewer.svg?style=for-the-badge
[contributors-url]: https://github.com/Onegold11/SteamNewsViewer/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Onegold11/SteamNewsViewer.svg?style=for-the-badge
[forks-url]: https://github.com/Onegold11/SteamNewsViewer/network/members
[stars-shield]: https://img.shields.io/github/stars/Onegold11/SteamNewsViewer.svg?style=for-the-badge
[stars-url]: https://github.com/Onegold11/SteamNewsViewer/stargazers
[issues-shield]: https://img.shields.io/github/issues/Onegold11/SteamNewsViewer.svg?style=for-the-badge
[issues-url]: https://github.com/Onegold11/SteamNewsViewer/issues
[license-shield]: https://img.shields.io/github/license/Onegold11/SteamNewsViewer.svg?style=for-the-badge
[license-url]: https://github.com/Onegold11/SteamNewsViewer/blob/master/LICENSE.txt
[product-screenshot]: image/screenshot.png
