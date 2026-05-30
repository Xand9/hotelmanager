const scrollStorageKey = `hotelmanager.scroll.${window.location.pathname}`;

const saveScrollPosition = () => {
    sessionStorage.setItem(scrollStorageKey, String(window.scrollY));
};

const restoreScrollPosition = () => {
    const savedPosition = sessionStorage.getItem(scrollStorageKey);

    if (savedPosition === null) {
        return;
    }

    sessionStorage.removeItem(scrollStorageKey);

    window.requestAnimationFrame(() => {
        window.scrollTo({
            top: Number(savedPosition),
            left: 0,
            behavior: "auto"
        });
    });
};

window.addEventListener("load", restoreScrollPosition);

document.addEventListener("submit", (event) => {
    const form = event.target;
    const message = form.dataset.confirm;

    if (message && !window.confirm(message)) {
        event.preventDefault();
        return;
    }

    saveScrollPosition();
});

const formatCpf = (value) => {
    const digits = value.replace(/\D/g, "").slice(0, 11);

    if (digits.length <= 3) {
        return digits;
    }

    if (digits.length <= 6) {
        return `${digits.slice(0, 3)}.${digits.slice(3)}`;
    }

    if (digits.length <= 9) {
        return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6)}`;
    }

    return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9)}`;
};

const formatTelefone = (value) => {
    const digits = value.replace(/\D/g, "").slice(0, 11);

    if (digits.length <= 2) {
        return digits;
    }

    if (digits.length <= 6) {
        return `(${digits.slice(0, 2)}) ${digits.slice(2)}`;
    }

    if (digits.length <= 10) {
        return `(${digits.slice(0, 2)}) ${digits.slice(2, 6)}-${digits.slice(6)}`;
    }

    return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7)}`;
};

document.querySelectorAll("[data-mask='cpf']").forEach((input) => {
    input.value = formatCpf(input.value);

    input.addEventListener("input", () => {
        input.value = formatCpf(input.value);
    });
});

document.querySelectorAll("[data-mask='telefone']").forEach((input) => {
    input.value = formatTelefone(input.value);

    input.addEventListener("input", () => {
        input.value = formatTelefone(input.value);
    });
});

document.querySelectorAll("[data-max-number]").forEach((input) => {
    const maxValue = Number(input.dataset.maxNumber);

    input.addEventListener("input", () => {
        if (input.value !== "" && Number(input.value) > maxValue) {
            input.value = String(maxValue);
        }
    });
});

const roomRows = Array.from(document.querySelectorAll("[data-room-row]"));
const roomFilterButtons = Array.from(document.querySelectorAll("[data-room-filter]"));
const roomFilterEmpty = document.querySelector("[data-room-filter-empty]");

const matchesRoomFilter = (row, filter) => {
    if (filter === "todos") {
        return true;
    }

    if (filter.startsWith("status:")) {
        return row.dataset.statusOperacional === filter.replace("status:", "");
    }

    if (filter === "reserva") {
        return row.dataset.temReserva === "true";
    }

    if (filter === "chamado") {
        return row.dataset.temChamado === "true";
    }

    if (filter === "solicitacao") {
        return row.dataset.tipoChamado === "SOLICITACAO_DO_HOSPEDE";
    }

    return true;
};

const applyRoomFilter = (filter) => {
    let visibleCount = 0;

    roomRows.forEach((row) => {
        const isVisible = matchesRoomFilter(row, filter);
        row.hidden = !isVisible;

        if (isVisible) {
            visibleCount += 1;
        }
    });

    if (roomFilterEmpty) {
        roomFilterEmpty.hidden = visibleCount > 0;
    }

    roomFilterButtons.forEach((button) => {
        const isActive = button.dataset.roomFilter === filter;
        button.classList.toggle("active", isActive);
        button.setAttribute("aria-pressed", String(isActive));
    });
};

roomFilterButtons.forEach((button) => {
    button.addEventListener("click", () => {
        applyRoomFilter(button.dataset.roomFilter);
    });
});
