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
            "email": "Teste",
            "senha": "123"
        },
        {
            "nome": "Isaac",
            "sobrenome": "Newton",
            "cpf": "1451561565",
            "endereco": "zona sul",
            "email": "Teste1",
            "senha": "123"
        },
        {
            "nome": "Icaro",
            "sobrenome": "Palmeiras",
            "cpf": "474747477",
            "endereco": "Fecap",
            "email": "Teste3",
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
            "nome": "Luz",
            "user_id": 2
        }
        
    ].each do |transaction_type_id|
     TransactionType.find_or_create_by!(transaction_type_id)
     end


     [
        {
            "valor": 3000,
            "data": "24/11/2004",
            "observacao": "teste",
            "transaction_type_id": 2,
            "user_id": 2
        },
        {
            "valor": 5000,
            "data": "11/10/2023",
            "observacao": "teste",
            "transaction_type_id": 2,
            "user_id": 2
        },
        {
            "valor": -3000,
            "data": "11/11/2011",
            "observacao": "teste",
            "transaction_type_id": 2,
            "user_id": 2
        }
        
    ].each do |transaction|
     Transaction.find_or_create_by!(transaction)
     end