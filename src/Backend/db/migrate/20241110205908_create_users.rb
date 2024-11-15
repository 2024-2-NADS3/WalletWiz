class CreateUsers < ActiveRecord::Migration[7.2]
  def change
    create_table :users do |t|
      t.string :nome
      t.string :sobrenome
      t.string :cpf
      t.string :endereco
      t.string :email
      t.string :senha

      t.timestamps
    end
  end
end
