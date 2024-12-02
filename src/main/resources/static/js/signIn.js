document.addEventListener('DOMContentLoaded', function () {
    const signInForm = document.getElementById('signInForm');

    signInForm.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 폼 제출 동작 방지

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // 로그인 API 호출
        fetch('/api/auth/sign-in', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('이메일 또는 비밀번호가 잘못되었습니다.');
                }
            })
            .then(data => {
                // AccessToken 저장
                localStorage.setItem('accessToken', data.accessToken);

                // 로그인 성공 메시지 및 페이지 이동
                alert('로그인 성공!');
                window.location.href = '/boardList.html'; // 게시판 페이지로 리다이렉트
            })
            .catch(error => {
                console.error('로그인 에러:', error.message);
                alert(error.message);
            });
    });
});
