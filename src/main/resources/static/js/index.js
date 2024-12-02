document.addEventListener('DOMContentLoaded', function () {
    const navLinks = document.querySelector('.nav-links');

    // 네비게이션 기본 링크
    const baseLinks = `
        <li><a href="/boardList.html">게시판</a></li>
        <li><a href="/signIn.html">로그인</a></li>
        <li><a href="/signUp.html">회원가입</a></li>
    `;

    // 링크 렌더링
    navLinks.innerHTML = baseLinks;
});