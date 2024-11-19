# This file should ensure the existence of records required to run the application in every environment (production,
# development, test). The code here should be idempotent so that it can be executed at any point in every environment.
# The data can then be loaded with the bin/rails db:seed command (or created alongside the database with db:setup).
#
# Example:
#
[
    {
        "nome": "Giovanne",
        "sobrenome": "Braga",
        "cpf": "41581511515",
        "endereco": "Tuiuti",
        "email": "giovanne@email.com",
        "senha": "123"
    },
    {
        "nome": "Isaac",
        "sobrenome": "Newton",
        "cpf": "1451561565",
        "endereco": "zona sul",
        "email": "isaac@email.com",
        "senha": "123"
    },
    {
        "nome": "Icaro",
        "sobrenome": "Palmeiras",
        "cpf": "474747477",
        "endereco": "Fecap",
        "email": "icaro@email.com",
        "senha": "123"
    }
].each do |user|
    User.find_or_create_by!(user)
    end

[
    {
        "nome": "Aluguel",
        "user_id": 1
    },
    {
        "nome": "Mercado",
        "user_id": 1
    },
    {
        "nome": "Casa",
        "user_id": 1
    },
    {
        "nome": "Lazer",
        "user_id": 1
    },
    {
        "nome": "Educação",
        "user_id": 1
    },
    {
        "nome": "Viagem",
        "user_id": 1
    },
    {
        "nome": "Transporte",
        "user_id": 1
    },
    {
        "nome": "Saúde",
        "user_id": 1
    },
    {
        "nome": "Salário",
        "user_id": 2
    },
    {
        "nome": "Renda Extra",
        "user_id": 2
    },
    {
        "nome": "Venda",
        "user_id": 2
    },
    {
        "nome": "Investimento",
        "user_id": 2
    },
    {
        "nome": "Outros",
        "user_id": 2
    }
].each do |transaction_type_id|
    TransactionType.find_or_create_by!(transaction_type_id)
    end

[
    {
        "nome": "teste",
        "valor": 3000,
        "data": "24/11/2004",
        "observacao": "teste",
        "transaction_type_id": 11,
        "user_id": 1
    },
    {
        "nome": "teste2",
        "valor": 5000,
        "data": "11/10/2023",
        "observacao": "teste",
        "transaction_type_id": 9,
        "user_id": 1
    },
    {
        "nome": "teste3",
        "valor": -3000,
        "data": "11/11/2011",
        "observacao": "teste",
        "transaction_type_id": 6,
        "user_id": 1
    }
].each do |transaction|
    Transaction.find_or_create_by!(transaction)
    end
