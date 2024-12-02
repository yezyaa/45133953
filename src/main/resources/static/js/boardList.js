document.addEventListener('DOMContentLoaded', function () {
    const navLinks = document.querySelector('.nav-links');
    const createPostButton = document.getElementById('createPostButton');

    // 로그인 상태 확인(localStorage에 accessToken이 있는지 확인)
    const isLoggedIn = !!localStorage.getItem('accessToken');

    // 네비게이션 링크 동적 변경
    navLinks.innerHTML = isLoggedIn
        ? `
            <li><a href="/index.html">홈</a></li>
            <li><a href="#" id="logoutLink">로그아웃</a></li>
        `
        : `
            <li><a href="/index.html">홈</a></li>
            <li><a href="/signIn.html">로그인</a></li>
            <li><a href="/signUp.html">회원가입</a></li>
        `;

    // 게시글 생성하기 버튼 클릭
    if (createPostButton) {
        createPostButton.addEventListener('click', function () {
            if (isLoggedIn) {
                // 로그인 상태: 게시글 작성 페이지로 이동
                window.location.href = '/boardCreate.html';
            } else {
                // 비로그인 상태: 로그인 페이지로 이동
                alert('로그인이 필요합니다.');
                window.location.href = '/signIn.html';
            }
        });
    }
});
