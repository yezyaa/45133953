document.addEventListener('DOMContentLoaded', function () {
    document.querySelector('.nav-links').innerHTML = `
        <li><a href="/index.html">홈</a></li>
        <li><a href="/signIn.html">로그인</a></li>
        <li><a href="/signUp.html">회원가입</a></li>
    `;

    const createPostButton = document.getElementById('createPostButton');

    // 로그인 상태 확인(로컬 스토리지에 accessToken이 있는지 확인)
    const isLoggedIn = !!localStorage.getItem('accessToken');

    // 게시글 생성하기 버튼 클릭
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
});
