const API_BASE_URL = 'http://localhost:8080/api';

// --- State Management ---
let state = {
    authToken: null,
    role: null, // 'ADMIN' or 'VOTER'
    username: null
};

// --- DOM Elements ---
const views = {
    login: document.getElementById('view-login'),
    admin: document.getElementById('view-admin'),
    voter: document.getElementById('view-voter'),
    results: document.getElementById('view-results')
};

// --- Utilities ---
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;

    document.getElementById('toast-container').appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'fadeIn 0.3s ease-in reverse forwards';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function switchView(viewName) {
    Object.values(views).forEach(v => v.classList.remove('active', 'hidden'));
    Object.values(views).forEach(v => {
        if (v !== views[viewName]) v.classList.add('hidden');
    });
    views[viewName].classList.add('active');
}

// --- API Helper ---
async function apiCall(endpoint, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (state.authToken) {
        headers['Authorization'] = `Basic ${state.authToken}`;
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            ...options,
            headers
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }

        // Handle empty responses
        const text = await response.text();
        return text ? JSON.parse(text) : {};
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// --- Auth Flow ---
document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const roleInput = document.getElementById('login-role').value;

    const token = btoa(`${username}:${password}`);

    // Attempt login by hitting a protected endpoint based on role
    // We don't have a dedicated /login endpoint, so we verify by trying to access a secure route.
    try {
        const testEndpoint = roleInput === 'ADMIN'
            ? '/election-result/getAllResults' // Authenticated
            : '/candidate/all'; // Authenticated

        // Set state optimistically to use in apiCall
        state.authToken = token;

        await apiCall(testEndpoint); // If this succeeds, credentials are valid

        state.role = roleInput; // Keep UI role mapped simply
        state.username = username;

        showToast(`Logged in successfully as ${state.role}`);

        if (state.role === 'ADMIN') {
            switchView('admin');
        } else {
            document.getElementById('voter-id-display').textContent = `ID: ${username}`;
            switchView('voter');
            loadCandidates();
        }
    } catch (error) {
        state.authToken = null;
        console.error("Login verification failed:", error);
        showToast(`Login failed: Please check credentials and role for ${username}`, 'error');
    }
});

const logout = () => {
    state = { authToken: null, role: null, username: null };
    document.getElementById('login-form').reset();
    switchView('login');
    showToast('Logged out successfully');
};

document.querySelectorAll('.logout-btn').forEach(btn => btn.addEventListener('click', logout));

// --- Admin Functions ---
document.getElementById('add-candidate-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('cand-name').value;
    const party = document.getElementById('cand-party').value;

    try {
        await apiCall('/candidate/add', {
            method: 'POST',
            body: JSON.stringify({ name, party })
        });
        showToast('Candidate added successfully!');
        e.target.reset();
    } catch (error) {
        showToast('Failed to add candidate.', 'error');
    }
});

document.getElementById('add-voter-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('voter-name').value;
    const email = document.getElementById('voter-email').value;

    try {
        const res = await apiCall('/voters/register', {
            method: 'POST',
            body: JSON.stringify({ name, email })
        });
        showToast(`Voter registered! ID: ${res.id}`);
        // Autofill password form
        document.getElementById('pwd-username').value = res.id;
        e.target.reset();
    } catch (error) {
        showToast('Failed to register voter entity.', 'error');
    }
});

document.getElementById('add-password-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const userId = document.getElementById('pwd-username').value;
    const password = document.getElementById('pwd-password').value;
    const role = document.getElementById('pwd-role').value;

    try {
        await apiCall('/voterPassword/addPassword', {
            method: 'POST',
            body: JSON.stringify({ userId, password, role })
        });
        showToast(`Authentication set for User ID: ${userId}`);
        e.target.reset();
    } catch (error) {
        showToast('Failed to set password.', 'error');
    }
});

document.getElementById('declare-result-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const electionName = document.getElementById('election-name-input').value;

    try {
        const result = await apiCall('/election-result/declare', {
            method: 'POST',
            body: JSON.stringify({ electionName })
        });
        showToast(`Result declared! Winner ID: ${result.winnerId}`);
        e.target.reset();
    } catch (error) {
        showToast('Failed to declare result.', 'error');
    }
});

// --- Voter Functions ---
async function loadCandidates() {
    const list = document.getElementById('candidates-list');
    list.innerHTML = '<div class="loading-spinner">Loading candidates...</div>';

    try {
        const candidates = await apiCall('/candidate/all');
        list.innerHTML = '';

        if (candidates.length === 0) {
            list.innerHTML = '<div class="loading-spinner">No candidates found.</div>';
            return;
        }

        candidates.forEach(cand => {
            const item = document.createElement('div');
            item.className = 'list-item';
            item.innerHTML = `
                <div class="item-details">
                    <h4>${cand.name}</h4>
                    <p>Party: ${cand.party}</p>
                </div>
                <button class="btn btn-primary" style="width: auto;" onclick="castVote(${cand.id})">Vote</button>
            `;
            list.appendChild(item);
        });
    } catch (error) {
        list.innerHTML = '<div class="loading-spinner">Error loading candidates.</div>';
    }
}

document.getElementById('refresh-candidates').addEventListener('click', loadCandidates);

window.castVote = async function (candidateId) {
    if (!confirm('Are you sure you want to vote for this candidate?')) return;

    try {
        await apiCall('/votes/cast', {
            method: 'POST',
            body: JSON.stringify({
                voterId: parseInt(state.username),
                candidateId: candidateId
            })
        });
        showToast('Vote cast successfully!');
    } catch (error) {
        showToast('Failed to cast vote. You might have already voted.', 'error');
    }
}

// --- View Results ---
document.getElementById('view-results-btn').addEventListener('click', async () => {
    switchView('results');
    const list = document.getElementById('results-list');
    list.innerHTML = '<div class="loading-spinner">Loading results...</div>';

    try {
        const results = await apiCall('/election-result/getAllResults');
        list.innerHTML = '';

        if (results.length === 0) {
            list.innerHTML = '<div class="loading-spinner">No results declared yet.</div>';
            return;
        }

        results.forEach(res => {
            const item = document.createElement('div');
            item.className = 'list-item';
            item.innerHTML = `
                <div class="item-details">
                    <h4>${res.electionName}</h4>
                    <p>Winner ID: ${res.winnerId} | Votes: ${res.winner.voteCount}</p>
                </div>
                <div style="text-align: right; color: var(--text-muted); font-size: 0.9rem;">
                    Total Votes: ${res.totalVotes}
                </div>
            `;
            list.appendChild(item);
        });
    } catch (error) {
        list.innerHTML = '<div class="loading-spinner">Error loading results.</div>';
    }
});

document.getElementById('back-to-admin').addEventListener('click', () => switchView('admin'));
