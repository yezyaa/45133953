document.addEventListener('DOMContentLoaded', function () {
    const navLinks = document.querySelector('.nav-links');
    const createPostButton = document.getElementById('createPostButton');
    const boardList = document.getElementById('boardList');
    const pagination = document.getElementById('pagination');
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');

    searchInput.value = ''; // 검색어 초기화

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

    // 로그아웃 시 API 호출(DB에 accessToken삭제) 및 로컬 스토리지 토큰 삭제
    if (isLoggedIn) {
        const logoutLink = document.getElementById('logoutLink');
        logoutLink.addEventListener('click', function (event) {
            event.preventDefault();

            const accessToken = localStorage.getItem('accessToken');

            // 로그아웃 API 호출
            fetch('/api/auth/sign-out', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'application/json',
                },
            })
                .then(response => {
                    if (response.ok) {
                        localStorage.removeItem('accessToken'); // 로컬 스토리지에서 토큰 삭제
                        alert('로그아웃 되었습니다.');
                        window.location.href = '/index.html'; // 홈 페이지로 이동
                    } else {
                        return response.text().then(err => {
                            throw new Error(err || '로그아웃 실패');
                        });
                    }
                })
                .catch(error => {
                    console.error('로그아웃 에러:', error.message);
                    alert(`로그아웃 실패: ${error.message}`);
                });
        });
    }

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

    // 검색 버튼 클릭 이벤트 추가
    searchButton.addEventListener('click', function () {
        const keyword = searchInput.value.trim();
        fetchBoardList(0, keyword); // 검색어와 함께 목록 조회
    });

    // 게시글 목록 조회
    fetchBoardList(0);

    function fetchBoardList(page = 0, keyword = '') {
        const accessToken = localStorage.getItem('accessToken');

        // 검색어를 포함한 URL 생성
        const url = keyword
            ? `/api/board?page=${page}&keyword=${encodeURIComponent(keyword)}`
            : `/api/board?page=${page}`;

        fetch(url, { // 이 부분에서 url 변수를 사용
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${accessToken}`,
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('게시글 목록을 불러오는 데 실패했습니다.');
                }
                return response.json();
            })
            .then(data => {
                const pageSize = data.data.size; // 페이지 크기
                const currentPage = data.data.number; // 현재 페이지 번호
                const totalElements = data.data.totalElements; // 전체 게시글 수
                renderBoardList(data.data.content, currentPage, pageSize, totalElements); // 게시글 목록 렌더링
                renderPagination(data); // 페이지네이션 렌더링
            })
            .catch(error => {
                console.error('목록 조회 에러:', error.message);
                alert(`목록 조회 실패: ${error.message}`);
            });
    }

    // 게시글 목록 렌더링
    function renderBoardList(boards, currentPage, pageSize, totalElements) {
        boardList.innerHTML = ''; // 기존 데이터 초기화

        boards.forEach((board, index) => {
            // 번호 계산: (현재 페이지 * 페이지 크기) + 현재 페이지 내 순번
            const boardNumber = totalElements - (currentPage * pageSize + index);

            const row = document.createElement('tr');

            // 행에 클릭 이벤트 추가
            row.addEventListener('click', () => {
                window.location.href = `/boardDetail.html?boardId=${board.id}`;
            });

            row.innerHTML = `
            <td>${boardNumber}</td>
            <td>${board.title}</td>
            <td>${board.hasAttachment ? '첨부파일 있음' : '첨부파일 없음'}</td>
            <td>${board.email}</td>
            <td>${new Date(board.createdAt).toLocaleString()}</td>
            <td>${board.views}</td>
        `;
            boardList.appendChild(row);
        });
    }

    // 페이지네이션 렌더링
    function renderPagination(data) {
        pagination.innerHTML = ''; // 기존 페이지네이션 초기화

        const { totalPages, number } = data.data; // 전체 페이지 수와 현재 페이지 가져오기
        const keyword = searchInput.value.trim(); // 현재 검색어 가져오기

        for (let i = 1; i <= totalPages; i++) {
            const button = document.createElement('button');
            button.textContent = i;
            button.className = i === number + 1 ? 'active' : ''; // number는 0부터 시작하므로 +1
            button.addEventListener('click', () => fetchBoardList(i - 1, keyword)); // 검색어와 함께 페이지 변경
            pagination.appendChild(button);
        }
    }
});
