document.addEventListener("submit", (event) => {
    const form = event.target;
    const message = form.dataset.confirm;
    if (message && !window.confirm(message)) {
        event.preventDefault();
    }
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
