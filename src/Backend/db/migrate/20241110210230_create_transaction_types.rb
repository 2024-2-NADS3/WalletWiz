class CreateTransactionTypes < ActiveRecord::Migration[7.2]
  def change
    create_table :transaction_types do |t|
      t.string :nome
      t.references :user, null: false, foreign_key: true

      t.timestamps
    end
  end
end
